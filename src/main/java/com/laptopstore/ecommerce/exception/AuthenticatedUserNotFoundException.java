package com.laptopstore.ecommerce.exception;

public class AuthenticatedUserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy người dùng. Vui lòng đăng nhập lại";

    public AuthenticatedUserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
