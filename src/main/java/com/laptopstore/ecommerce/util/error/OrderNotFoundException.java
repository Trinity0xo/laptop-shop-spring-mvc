package com.laptopstore.ecommerce.util.error;

public class OrderNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy đơn hàng";

    public OrderNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public OrderNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
