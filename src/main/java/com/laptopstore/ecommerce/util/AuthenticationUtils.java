package com.laptopstore.ecommerce.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static String getAuthenticatedName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String name = authentication.getName();
            if (name != null && !name.isEmpty()) {
                return name;
            }
        }
        return null;
    }
}
