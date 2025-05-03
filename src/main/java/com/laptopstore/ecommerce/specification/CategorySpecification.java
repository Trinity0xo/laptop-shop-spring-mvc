package com.laptopstore.ecommerce.specification;

import org.springframework.data.jpa.domain.Specification;

import com.laptopstore.ecommerce.model.Category;

public class CategorySpecification {
    public static Specification<Category> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
}
