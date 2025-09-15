package com.laptopstore.ecommerce.exception;

import lombok.Getter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public class AuthUserNotFoundException extends UsernameNotFoundException {
    private final String username;

    public AuthUserNotFoundException(String username) {
        super("Authenticated user not found: " + username);
        this.username = username;
    }
}