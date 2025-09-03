package com.laptopstore.ecommerce.util.error;

public class ConflictException extends RuntimeException {
    String redirectUrl;

    public ConflictException(String message){
        super(message);
    }

    public ConflictException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public boolean hasRedirect() {
        return this.redirectUrl != null && !this.redirectUrl.isEmpty();
    }
}
