package com.laptopstore.ecommerce.util.error;

public class ProductNotFoundException extends EntityNotFoundException{
    private static final String DEFAULT_MESSAGE = "Không tìm thấy sản phẩm";

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
