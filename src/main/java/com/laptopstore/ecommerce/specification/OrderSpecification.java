package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrderSpecification {

    public static Specification<Order> isUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Order> equalId(Long id){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Order> emailLike(String receiverEmail) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("receiverEmail"),
                "%" + receiverEmail + "%");
    }

    public static Specification<Order> paymentMethodIn(List<PaymentMethodEnum> paymentMethods) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("paymentMethod")).value(paymentMethods);
    }

    public static Specification<Order> orderStatusIn(List<OrderStatusEnum> orderStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("orderStatus")).value(orderStatus);
    }

    public static Specification<Order> minTotalPrice(Double minTotalPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("totalPrice"), minTotalPrice);
    }

    public static Specification<Order> maxTotalPrice(Double maxTotalPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get("totalPrice"), maxTotalPrice);
    }

    public static Specification<Order> totalPriceBetween(Double minTotalPrice, Double maxTotalPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.gt(root.get("totalPrice"), minTotalPrice),
                criteriaBuilder.le(root.get("totalPrice"), maxTotalPrice));
    }
}
