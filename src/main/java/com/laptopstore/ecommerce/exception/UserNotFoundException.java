package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy người dùng";
    private final Long userId;

    public UserNotFoundException(Long userId) {
        super(DEFAULT_MESSAGE);
        this.userId = userId;
    }
}
