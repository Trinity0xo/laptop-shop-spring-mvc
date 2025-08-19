package com.laptopstore.ecommerce.util.error;

public class ReviewNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy Đánh giá";

    public ReviewNotFoundException(){

    }

    public ReviewNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
