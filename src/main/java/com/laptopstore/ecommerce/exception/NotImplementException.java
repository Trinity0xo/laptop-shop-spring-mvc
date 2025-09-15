package com.laptopstore.ecommerce.exception;

public class NotImplementException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Chức năng chưa hoàn thành";

    public NotImplementException() {
        super(DEFAULT_MESSAGE);
    }
}
