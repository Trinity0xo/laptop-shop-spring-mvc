package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class BrandNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy Hãng";
    private final Long brandId;

    public BrandNotFoundException(Long brandId) {
        super(DEFAULT_MESSAGE);
        this.brandId = brandId;
    }
}
