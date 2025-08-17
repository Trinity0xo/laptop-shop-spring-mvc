package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> emailLike(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> notEqualId(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), id);
    }
}
