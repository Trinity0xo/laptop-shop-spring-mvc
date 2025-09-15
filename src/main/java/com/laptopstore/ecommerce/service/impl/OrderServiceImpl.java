package com.laptopstore.ecommerce.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.dto.order.OrderFilterDto;
import com.laptopstore.ecommerce.dto.order.UserCancelOrderDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.exception.*;
import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.repository.*;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.specification.OrderSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import org.springframework.transaction.annotation.Transactional;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;
import static com.laptopstore.ecommerce.util.PriceUtils.getValidPriceRange;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order createNewOrder(CheckoutDto checkoutDto) {
        User user = this.userRepository.findByEmail(checkoutDto.getEmail()).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(checkoutDto.getEmail());
        }

        List<CheckoutDto.CheckoutProduct> checkoutProducts = checkoutDto.getCheckoutProducts();
        if (checkoutProducts == null || checkoutProducts.isEmpty()) {
            throw new BadRequestException("Không có sản phẩm để thanh toán");
        }

        Map<Long, Product> validCheckoutProducts = new HashMap<>();


        // n + 1 (not good)
        for (CheckoutDto.CheckoutProduct checkoutProduct : checkoutProducts){
            Product product = this.productRepository.findById(checkoutProduct.getId()).orElse(null);
            if(product == null){
               throw new ProductNotFoundException(checkoutProduct.getId());
            }

            if(checkoutProduct.getQuantity() > product.getQuantity()){
                throw new BadRequestException("Số lượng trong kho không đủ");
            }

            validCheckoutProducts.put(product.getId(), product);
        }

        Order order = new Order(
                user,
                checkoutDto.getFirstName(),
                checkoutDto.getLastName(),
                checkoutDto.getEmail(),
                checkoutDto.getAddress(),
                checkoutDto.getPhone(),
                checkoutDto.getNote()
        );

        List<OrderItem> newOrderItem = new ArrayList<>();
        for (CheckoutDto.CheckoutProduct checkoutProduct: checkoutDto.getCheckoutProducts()){
            Product product = validCheckoutProducts.get(checkoutProduct.getId());

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    checkoutProduct.getPrice(),
                    checkoutProduct.getQuantity()
            );

            order.setTotalPrice(order.getTotalPrice() + checkoutProduct.getProductTotal());
            newOrderItem.add(orderItem);

            product.setQuantity(product.getQuantity() - checkoutProduct.getQuantity());
            this.productRepository.save(product);
        }

        order.setOrderItems(newOrderItem);
        this.orderRepository.save(order);

        List<CartItem> needRemove = new ArrayList<>();
        Cart userCart = user.getCart();
        if(userCart != null && !userCart.getCartItems().isEmpty()){
            List<CartItem> userCartItems = userCart.getCartItems();
            for(CheckoutDto.CheckoutProduct checkoutProduct : checkoutDto.getCheckoutProducts()){
                for (CartItem cartItem : userCartItems){
                    if(cartItem.getProduct().getId().equals(checkoutProduct.getId())){
                        if(checkoutProduct.getQuantity() >= cartItem.getQuantity()){
                            needRemove.add(cartItem);
                        }else{
                            cartItem.setQuantity(cartItem.getQuantity() - checkoutProduct.getQuantity());
                        }
                    }
                }
            }

            if(!needRemove.isEmpty()){
                for (CartItem item : needRemove){
                    userCartItems.remove(item);
                }

               this.cartRepository.save(userCart);
            }
        }

        return order;
    }

    @Override
    public PageResponse<List<Order>> getUserOrderHistory(String email, OrderFilterDto orderFilterDto){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        Specification<Order> specification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user)
        );

        Pageable pageable = PaginationUtils.createPageable(
                orderFilterDto.getIntegerPage(),
                orderFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Order.class, orderFilterDto.getSortBy(), Order.DEFAULT_SORT_FIELD),
                orderFilterDto.getEnumSortDirection()
        );

        if (orderFilterDto.getSearch() != null && !orderFilterDto.getSearch().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecifications.equalId(orderFilterDto.getLongSearch());
            specification = specification.and(currentSpecification);
        }

        if (orderFilterDto.getEnumOrderStatus() != null && !orderFilterDto.getEnumOrderStatus().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecifications
                    .orderStatusIn(orderFilterDto.getEnumOrderStatus());
            specification = specification.and(currentSpecification);
        }

        if (orderFilterDto.getEnumPaymentMethods() != null && !orderFilterDto.getEnumPaymentMethods().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecifications
                    .paymentMethodIn(orderFilterDto.getEnumPaymentMethods());
            specification = specification.and(currentSpecification);
        }

        Map<String, Double> priceRange = getValidPriceRange(
                orderFilterDto.getDoubleMinTotalPrice(),
                orderFilterDto.getDoubleMaxTotalPrice(),
                OrderFilterDto.MIN_TOTAL_PRICE,
                OrderFilterDto.MAX_TOTAL_PRICE
        );

        if (!priceRange.isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecifications
                    .totalPriceBetween(priceRange.get("min"), priceRange.get("max"));
            specification = specification.and(currentSpecification);
        }

        Map<String, Instant> instantRange = getValidInstantRange(
                orderFilterDto.getInstantStartDate(),
                orderFilterDto.getInstantEndDate()
        );

        if(!instantRange.isEmpty()){
            Specification<Order> currentSpecification = OrderSpecifications
                    .dateBetween(instantRange.get("startDate"), instantRange.get("endDate"));
            specification = specification.and(currentSpecification);
        }

        Page<Order> orders = this.orderRepository.findAll(specification, pageable);

        return new PageResponse<>(
                orders.getPageable().getPageNumber() + 1,
                orders.getTotalPages(),
                orders.getTotalElements(),
                orders.getContent()
        );
    }

    @Override
    public Order getUserOrderItems(String email, long orderId) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        Order userOrder = this.orderRepository.findByIdAndUser(orderId, user).orElse(null);
        if(userOrder == null){
            throw new OrderNotFoundException(orderId);
        }

        return userOrder;
    }

    @Override
    public UserCancelOrderDto getInformationForUserCancelOrder(String email, long orderId){
        Order order = this.getUserOrderItems(email, orderId);
        return new UserCancelOrderDto(order.getId());
    }

    @Override
    @Transactional
    public void userCancelOrder(String email, UserCancelOrderDto userCancelOrderDto) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
             throw new AuthUserNotFoundException(email);
        }

        Order userOrder = this.orderRepository.findByIdAndUser(userCancelOrderDto.getId(), user).orElse(null);
        if(userOrder == null){
            throw new OrderNotFoundException(userCancelOrderDto.getId());
        }

        if(!userOrder.getStatus().equals(OrderStatusEnum.PENDING)){
            throw new BadRequestException("Bạn chỉ có thể huỷ đơn hàng khi trạng thái đang là 'Đang xử lý'");
        }

        List<OrderItem> orderItems = userOrder.getOrderItems();
        List<Product> updatedProducts = new ArrayList<>();
        for(OrderItem orderItem : orderItems){
            Product product = orderItem.getProduct();
            if(product != null){
                product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                updatedProducts.add(product);
            }
        }

        this.productRepository.saveAll(updatedProducts);

        userOrder.setStatus(OrderStatusEnum.CANCELLED);
        userOrder.setCancelledBy(user);
        userOrder.setCancelledAt(Instant.now());
        userOrder.setCancelledReason(userCancelOrderDto.getCancelledReason());

        this.orderRepository.save(userOrder);
    }

    @Override
    public PageResponse<List<Order>> getAllOrders(OrderFilterDto orderFilterDto){
        Specification<Order> specification = Specification.where(null);

        Pageable pageable = PaginationUtils.createPageable(
                orderFilterDto.getIntegerPage(),
                orderFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Order.class, orderFilterDto.getSortBy(), Order.DEFAULT_SORT_FIELD),
                orderFilterDto.getEnumSortDirection()
        );


        Map<String, Instant> instantRange = getValidInstantRange(
                orderFilterDto.getInstantStartDate(),
                orderFilterDto.getInstantEndDate()
        );

        if(!instantRange.isEmpty()){
            Specification<Order> currentSpecification = OrderSpecifications
                    .dateBetween(instantRange.get("startDate"), instantRange.get("endDate"));
            specification = specification.and(currentSpecification);
        }


        Map<String, Double> priceRange = getValidPriceRange(
                orderFilterDto.getDoubleMinTotalPrice(),
                orderFilterDto.getDoubleMaxTotalPrice(),
                OrderFilterDto.MIN_TOTAL_PRICE,
                OrderFilterDto.MAX_TOTAL_PRICE
        );

        if (!priceRange.isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecifications
                    .totalPriceBetween(priceRange.get("min"), priceRange.get("max"));
            specification = specification.and(currentSpecification);
        }

        if(orderFilterDto.getEnumPaymentMethods() != null && !orderFilterDto.getEnumPaymentMethods().isEmpty()){
            Specification<Order> currentSpecification = OrderSpecifications.paymentMethodIn(orderFilterDto.getEnumPaymentMethods());
            specification = specification.and(currentSpecification);
        }

        if(orderFilterDto.getEnumOrderStatus() != null && !orderFilterDto.getEnumOrderStatus().isEmpty()){
            Specification<Order> currentSpecification = OrderSpecifications.orderStatusIn(orderFilterDto.getEnumOrderStatus());
            specification = specification.and(currentSpecification);
        }

        if(orderFilterDto.getSearch() != null && !orderFilterDto.getSearch().isEmpty()){
            Specification<Order> currentSpecification = OrderSpecifications.equalId(orderFilterDto.getLongSearch());
            specification = specification.and(currentSpecification);
        }

        Page<Order> orders = this.orderRepository.findAll(specification, pageable);

        return new PageResponse<>(
                orders.getPageable().getPageNumber() + 1,
                orders.getTotalPages(),
                orders.getTotalElements(),
                orders.getContent()
        );
    }

    @Override
    public Order getOrderItems(long orderId){
        Order order = this.orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(orderId);
        }

        return order;
    }

    @Override
    public UpdateOrderStatusDto getOrderStatusUpdateInformation(long orderId){
        Order order = this.orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(orderId);
        }

        return new UpdateOrderStatusDto(
                order.getId(),
                order.getStatus(),
                order.getCancelledReason()
        );
    }

    @Override
    @Transactional
    public void updateOrderStatus(String email, UpdateOrderStatusDto updateOrderStatusDto) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        Order order = this.orderRepository.findById(updateOrderStatusDto.getId()).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(updateOrderStatusDto.getId());
        }

        if (updateOrderStatusDto.getStatus().equals(OrderStatusEnum.CANCELLED)) {
            if(order.getStatus().equals(OrderStatusEnum.DELIVERED)){
                throw new BadRequestException("Không thể hủy đơn hàng khi đã giao");
            }

            List<OrderItem> orderItems = order.getOrderItems();
            List<Product> updatedProducts = new ArrayList<>();
            for(OrderItem orderItem : orderItems){
                Product product = orderItem.getProduct();
                if(product != null){
                    product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                    updatedProducts.add(product);
                }
            }

            this.productRepository.saveAll(updatedProducts);

            order.setCancelledReason(updateOrderStatusDto.getCancelledReason());
            order.setCancelledBy(user);
            order.setCancelledAt(Instant.now());
        } else if(updateOrderStatusDto.getStatus().equals(OrderStatusEnum.DELIVERED)){
            order.setDeliveredAt(Instant.now());
        }

        order.setStatus(updateOrderStatusDto.getStatus());

        this.orderRepository.save(order);
    }
}
