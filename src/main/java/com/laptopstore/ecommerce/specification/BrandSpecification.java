package com.laptopstore.ecommerce.specification;

import org.springframework.data.jpa.domain.Specification;

import com.laptopstore.ecommerce.model.Brand;

public class BrandSpecification {
    public static Specification<Brand> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
}
