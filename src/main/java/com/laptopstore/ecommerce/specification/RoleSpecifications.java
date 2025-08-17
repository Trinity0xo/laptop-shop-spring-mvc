package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.model.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {
    public static Specification<Role> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
}
