package com.laptopstore.ecommerce.specification;

import org.springframework.data.jpa.domain.Specification;

import com.laptopstore.ecommerce.model.Product;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> categoryIn(List<String> categoryNames) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("name")).value(categoryNames);
    }

    public static Specification<Product> brandIn(List<String> brandNames) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("brand").get("name")).value(brandNames);
    }

    public static Specification<Product> minPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get("price"), maxPrice);
    }

    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.ge(root.get("price"), minPrice),
                criteriaBuilder.le(root.get("price"), maxPrice));
    }

    public static Specification<Product> discountPriceBetween(Double minDiscountPrice, Double maxDiscountPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.ge(root.get("discountPrice"), minDiscountPrice),
                criteriaBuilder.le(root.get("discountPrice"), maxDiscountPrice));
    }

    public static Specification<Product> stockStatus(Boolean inStock) {
        return (root, query, criteriaBuilder) -> {
            if (!inStock) {
                return criteriaBuilder.le(root.get("quantity"), 0);
            } else {
                return criteriaBuilder.gt(root.get("quantity"), 0);
            }
        };
    }
}
