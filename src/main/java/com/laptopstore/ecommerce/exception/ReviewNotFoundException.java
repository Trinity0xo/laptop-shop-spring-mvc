package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class ReviewNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy Đánh giá";
    private final Long reviewId;

    public ReviewNotFoundException(Long reviewId){
        super(DEFAULT_MESSAGE);
        this.reviewId = reviewId;
    }
}
