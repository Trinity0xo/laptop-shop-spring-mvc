package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy danh mục";
    private final Long categoryId;

    public CategoryNotFoundException(Long categoryId) {
        super(DEFAULT_MESSAGE);
        this.categoryId = categoryId;
    }
}
