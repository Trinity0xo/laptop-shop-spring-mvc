package com.laptopstore.ecommerce.util.error;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy người dùng";

    public UserNotFoundException() {

    }

    public UserNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
