package com.laptopstore.ecommerce.util;

import com.laptopstore.ecommerce.configuration.CustomOAuth2User;
import com.laptopstore.ecommerce.configuration.CustomUserPrincipal;
import com.laptopstore.ecommerce.dto.auth.AuthenticatedInformationDto;
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

    public static AuthenticatedInformationDto getAuthenticatedInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            AuthenticatedInformationDto dto = new AuthenticatedInformationDto();

            if (principal instanceof CustomUserPrincipal customUserPrincipal) {
                dto.setEmail(customUserPrincipal.getUsername());
                dto.setFullName(customUserPrincipal.getFullName());
                dto.setAvatar(customUserPrincipal.getAvatar());
                dto.setCartItemCount(customUserPrincipal.getCartItemCount());

                if (!customUserPrincipal.getAuthorities().isEmpty()) {
                    String role = customUserPrincipal.getAuthorities().iterator().next().getAuthority();
                    dto.setRoleName(role);
                    dto.setRoleSlug(role.replace("ROLE_", "").toUpperCase());
                }

                return dto;
            }else if (principal instanceof CustomOAuth2User customOAuth2User){
                dto.setEmail(customOAuth2User.getAttribute("email"));
                dto.setFullName(customOAuth2User.getFullName());
                dto.setAvatar(customOAuth2User.getAvatar());
                dto.setCartItemCount(customOAuth2User.getCartItemCount());

                if (!customOAuth2User.getAuthorities().isEmpty()) {
                    String role = customOAuth2User.getAuthorities().iterator().next().getAuthority();
                    dto.setRoleName(role);
                    dto.setRoleSlug(role.replace("ROLE_", "").toUpperCase());
                }

                return dto;
            }
        }
        return null;
    }
}
