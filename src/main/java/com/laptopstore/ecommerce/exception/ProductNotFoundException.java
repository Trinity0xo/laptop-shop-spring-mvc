package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Không tìm thấy sản phẩm";
    private Long productId;
    private String productSlug;

    public ProductNotFoundException(Long productId) {
        super(DEFAULT_MESSAGE);
        this.productId = productId;
    }

    public ProductNotFoundException(String productSlug) {
        super(DEFAULT_MESSAGE);
        this.productSlug = productSlug;
    }
}
