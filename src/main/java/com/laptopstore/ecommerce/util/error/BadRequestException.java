package com.laptopstore.ecommerce.util.error;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    String redirectUrl;

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public boolean hasRedirect() {
        return this.redirectUrl != null && !this.redirectUrl.isEmpty();
    }
}
