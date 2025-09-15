package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy đơn hàng";
    private final Long orderId;

    public OrderNotFoundException(Long orderId){
        super(DEFAULT_MESSAGE);
        this.orderId = orderId;
    }
}
