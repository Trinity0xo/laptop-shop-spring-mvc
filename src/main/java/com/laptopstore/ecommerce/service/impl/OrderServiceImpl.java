package com.laptopstore.ecommerce.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import com.laptopstore.ecommerce.dto.order.OrderFilterDto;
import com.laptopstore.ecommerce.dto.order.CancelOrderInformationDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.repository.*;
import com.laptopstore.ecommerce.service.OrderService;
import com.laptopstore.ecommerce.specification.OrderSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.error.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;
import static com.laptopstore.ecommerce.util.PriceUtils.getValidPriceRange;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartItemsRepository cartItemsRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;

    public OrderServiceImpl(UserRepository userRepository, CartItemsRepository cartItemsRepository, ProductRepository productRepository, OrderRepository orderRepository,
                            OrderItemsRepository orderItemsRepository) {
        this.userRepository = userRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public void createNewOrder(CheckoutDto checkoutDto) {
        User user = this.userRepository.findByEmail(checkoutDto.getEmail()).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        List<CheckoutDto.CheckoutProduct> checkoutProducts = checkoutDto.getCheckoutProducts();
        if (checkoutProducts == null || checkoutProducts.isEmpty()) {
            throw new BadRequestException("Không có sản phẩm để thanh toán", "/cart");
        }

        Map<Long, Product> validCheckoutProducts = new HashMap<>();


        // n + 1 (not good)
        for (CheckoutDto.CheckoutProduct checkoutProduct : checkoutProducts){
            Product product = this.productRepository.findById(checkoutProduct.getId()).orElse(null);
            if(product == null){
               throw new ProductNotFoundException("/cart");
            }

            if(checkoutProduct.getQuantity() > product.getQuantity()){
                throw new StockUnavailableException("/cart");
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

        order = this.orderRepository.save(order);

        for (CheckoutDto.CheckoutProduct checkoutProduct: checkoutDto.getCheckoutProducts()){
            Product product = validCheckoutProducts.get(checkoutProduct.getId());

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    checkoutProduct.getPrice(),
                    checkoutProduct.getQuantity()
            );

            order.setTotalPrice(order.getTotalPrice() + checkoutProduct.getProductTotal());

            this.orderItemsRepository.save(orderItem);

            product.setQuantity(product.getQuantity() - checkoutProduct.getQuantity());
            this.productRepository.save(product);
        }

        this.orderRepository.save(order);

        List<CartItem> needRemove = new ArrayList<>();

        for(CheckoutDto.CheckoutProduct checkoutProduct : checkoutDto.getCheckoutProducts()){
            if(user.getCart() != null && !user.getCart().getCartItems().isEmpty()){
                for (CartItem cartItem : user.getCart().getCartItems()){
                    if(cartItem.getProduct().getId().equals(checkoutProduct.getId())){
                        if(checkoutProduct.getQuantity() >= cartItem.getQuantity()){
                            needRemove.add(cartItem);
                        }else{
                            cartItem.setQuantity(cartItem.getQuantity() - checkoutProduct.getQuantity());
                            this.cartItemsRepository.save(cartItem);
                        }
                    }
                }
            }
        }

        if(!needRemove.isEmpty()){
            this.cartItemsRepository.deleteAll(needRemove);
        }
    }

    @Override
    public PageResponse<List<Order>> getUserOrderHistory(String email, OrderFilterDto orderFilterDto){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
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
            throw new AuthenticatedUserNotFoundException();
        }

        Order userOrder = this.orderRepository.findByIdAndUser(orderId, user).orElse(null);
        if(userOrder == null){
            throw new OrderNotFoundException();
        }

        return userOrder;
    }

    @Override
    public CancelOrderInformationDto getInformationForUserCancelOrder(String email, long orderId){
        Order order = this.getUserOrderItems(email, orderId);
        return new CancelOrderInformationDto(order.getId());
    }

    @Override
    public void userCancelOrder(String email, CancelOrderInformationDto cancelOrderInformationDto) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
             throw new AuthenticatedUserNotFoundException();
        }

        Order userOrder = this.orderRepository.findByIdAndUser(cancelOrderInformationDto.getId(), user).orElse(null);
        if(userOrder == null){
            throw new OrderNotFoundException("/account/order-history");
        }

        if(!userOrder.getStatus().equals(OrderStatusEnum.PENDING)){
            throw new BadRequestException("Bạn chỉ có thể huỷ đơn hàng khi trạng thái đang là 'Đang xử lý'", "/account/order-history/details/" + userOrder.getId());
        }

        userOrder.setStatus(OrderStatusEnum.CANCELLED);
        userOrder.setCancelledBy(user);
        userOrder.setCancelledAt(Instant.now());

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
            throw new OrderNotFoundException();
        }

        return order;
    }

    @Override
    public UpdateOrderStatusDto getOrderStatusUpdateInformation(long orderId){
        Order order = this.orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException("/dashboard/order");
        }

        return new UpdateOrderStatusDto(
                order.getId(),
                order.getStatus(),
                order.getCancelledReason()
        );
    }

    @Override
    public void updateOrderStatus(String email, UpdateOrderStatusDto updateOrderStatusDto) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthenticatedUserNotFoundException();
        }

        Order order = this.orderRepository.findById(updateOrderStatusDto.getId()).orElse(null);
        if(order == null){
            throw new OrderNotFoundException("/dashboard/order");
        }

        if (updateOrderStatusDto.getStatus().equals(OrderStatusEnum.CANCELLED)) {
            if(order.getStatus().equals(OrderStatusEnum.DELIVERED)){
                throw new BadRequestException("Không thể hủy đơn hàng khi đã giao", "/dashboard/order/details/" + order.getId());
            }

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
