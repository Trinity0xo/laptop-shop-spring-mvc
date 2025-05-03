package com.laptopstore.ecommerce.service;

import java.util.List;

import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.specification.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.cart.CheckOutDto;
import com.laptopstore.ecommerce.dto.order.OrderCriteriaDto;
import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.repository.OrderDetailsRepository;
import com.laptopstore.ecommerce.repository.OrderRepository;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PageableService pageableService;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderService(OrderRepository orderRepository, PageableService pageableService,
            OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.pageableService = pageableService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public Order handleGetOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order handleGetOrderByIdAndUser(Long id, User user) {
        return this.orderRepository.findByIdAndUser(id, user).orElse(null);
    }

    public Page<Order> handleGetAllOrders(OrderCriteriaDto orderCriteriaDto) {
        Specification<Order> specification = Specification.where(null);

        Pageable pageable = pageableService.handleCreatePageable(orderCriteriaDto.getIntegerPage(),
                orderCriteriaDto.getIntegerLimit(), orderCriteriaDto.getSortBy(),
                orderCriteriaDto.getEnumSortDirection());

        if (orderCriteriaDto.getReceiverEmail() != null && !orderCriteriaDto.getReceiverEmail().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .emailLike(orderCriteriaDto.getReceiverEmail());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getOrderStatus() != null && !orderCriteriaDto.getOrderStatus().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .orderStatusIn(orderCriteriaDto.getOrderStatus());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getPaymentMethods() != null && !orderCriteriaDto.getPaymentMethods().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .paymentMethodIn(orderCriteriaDto.getPaymentMethods());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getDoubleMinTotalPrice() != null && orderCriteriaDto.getDoubleMaxTotalPrice() != null
                && orderCriteriaDto.getDoubleMaxTotalPrice() >= orderCriteriaDto.getDoubleMinTotalPrice()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .totalPriceBetween(orderCriteriaDto.getDoubleMinTotalPrice(), orderCriteriaDto.getDoubleMaxTotalPrice());
            specification = specification.and(currentSpecification);
        }

        return this.orderRepository.findAll(specification, pageable);
    }

    public Page<Order> handleGetAllOrders(OrderCriteriaDto orderCriteriaDto, User user) {
        Specification<Order> specification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user)
        );

        Pageable pageable = pageableService.handleCreatePageable(orderCriteriaDto.getIntegerPage(),
                orderCriteriaDto.getIntegerLimit(), orderCriteriaDto.getSortBy(),
                orderCriteriaDto.getEnumSortDirection());

        if (orderCriteriaDto.getReceiverEmail() != null && !orderCriteriaDto.getReceiverEmail().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .emailLike(orderCriteriaDto.getReceiverEmail());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getOrderStatus() != null && !orderCriteriaDto.getOrderStatus().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .orderStatusIn(orderCriteriaDto.getOrderStatus());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getPaymentMethods() != null && !orderCriteriaDto.getPaymentMethods().isEmpty()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .paymentMethodIn(orderCriteriaDto.getPaymentMethods());
            specification = specification.and(currentSpecification);
        }

        if (orderCriteriaDto.getDoubleMinTotalPrice() != null && orderCriteriaDto.getDoubleMaxTotalPrice() != null
                && orderCriteriaDto.getDoubleMaxTotalPrice() >= orderCriteriaDto.getDoubleMinTotalPrice()) {
            Specification<Order> currentSpecification = OrderSpecification
                    .totalPriceBetween(orderCriteriaDto.getDoubleMinTotalPrice(), orderCriteriaDto.getDoubleMaxTotalPrice());
            specification = specification.and(currentSpecification);
        }

        if(orderCriteriaDto.getLongOrderId() != null && orderCriteriaDto.getLongOrderId() > 0) {
            Specification<Order> currentSpecification = OrderSpecification.equalId(orderCriteriaDto.getLongOrderId());
            specification = specification.and(currentSpecification);
        }

        return this.orderRepository.findAll(specification, pageable);
    }

    public void handleUpdateOrderStatus(Order order, UpdateOrderStatusDto updateOrderStatusDto) {
        if (updateOrderStatusDto.getOrderStatus().equals(OrderStatusEnum.CANCELLED)) {
            order.setCancelledReason(updateOrderStatusDto.getCancelledReason());
        } else {
            order.setCancelledReason("");
        }

        order.setOrderStatus(updateOrderStatusDto.getOrderStatus());

        this.orderRepository.save(order);
    }

    public void handleCreateNewOrder(User user, CheckOutDto checkOutDto, List<CartDetails> cartDetails) {
        Order order = new Order();

        order.setUser(user);
        order.setReceiverFirstName(checkOutDto.getReceiverFirstName());
        order.setReceiverLastName(checkOutDto.getReceiverLastName());
        order.setReceiverEmail(checkOutDto.getReceiverEmail());
        order.setReceiverPhone(checkOutDto.getReceiverPhone());
        order.setReceiverAddress(checkOutDto.getReceiverAddress());
        order.setReceiverNote(checkOutDto.getReceiverNote());
        order.setOrderStatus(OrderStatusEnum.PENDING);

        order = this.orderRepository.save(order);

        for (CartDetails cartDetail : cartDetails) {
            OrderDetails orderDetails = new OrderDetails();
            long productQuantity = cartDetail.getQuantity();
            double productSum = cartDetail.getProduct().getDiscountPrice() * productQuantity;

            orderDetails.setProduct(cartDetail.getProduct());
            orderDetails.setPrice(cartDetail.getProduct().getDiscountPrice());
            orderDetails.setQuantity(productQuantity);
            orderDetails.setSum(productSum);

            orderDetails.setOrder(order);

            order.setTotalPrice(order.getTotalPrice() + productSum);

            this.orderDetailsRepository.save(orderDetails);
        }

        this.orderRepository.save(order);
    }
}
