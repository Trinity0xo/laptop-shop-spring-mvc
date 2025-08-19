package com.laptopstore.ecommerce.util.error;

public class EntityNotFoundException extends RuntimeException {
    String redirectUrl;

    public EntityNotFoundException(){
    }

    public EntityNotFoundException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public boolean hasRedirect() {
        return redirectUrl != null && !redirectUrl.isEmpty();
    }
}
