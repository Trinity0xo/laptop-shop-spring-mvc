package com.laptopstore.ecommerce.util.error;

public class BrandNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy Hãng";

    public BrandNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BrandNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
