package com.laptopstore.ecommerce.util.error;

public class NotImplementException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Chức năng chưa hoàn thành";
    String redirectUrl;

    public NotImplementException(String redirectUrl) {
        super(DEFAULT_MESSAGE);
        this.redirectUrl = redirectUrl;
    }
}
