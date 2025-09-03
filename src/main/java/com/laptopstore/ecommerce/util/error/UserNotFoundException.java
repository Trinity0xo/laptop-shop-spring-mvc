package com.laptopstore.ecommerce.util.error;

public class UserNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy người dùng";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
