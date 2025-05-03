package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.User;
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

    public static Specification<Order> paymentMethodIn(List<String> paymentMethods) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("paymentMethod")).value(paymentMethods);
    }

    public static Specification<Order> orderStatusIn(List<String> orderStatus) {
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
