package com.laptopstore.ecommerce.util;

import com.laptopstore.ecommerce.configuration.CustomOAuth2User;
import com.laptopstore.ecommerce.configuration.CustomUserPrincipal;
import com.laptopstore.ecommerce.dto.auth.AuthenticatedInformationDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
                return getAuthenticatedInformationDto(dto, customUserPrincipal.getFullName(), customUserPrincipal.getAvatar(), customUserPrincipal.getCartItemCount(), customUserPrincipal.getAuthorities().isEmpty(), customUserPrincipal.getAuthorities().iterator().next());
            }else if (principal instanceof CustomOAuth2User customOAuth2User){
                dto.setEmail(customOAuth2User.getAttribute("email"));
                return getAuthenticatedInformationDto(dto, customOAuth2User.getFullName(), customOAuth2User.getAvatar(), customOAuth2User.getCartItemCount(), customOAuth2User.getAuthorities().isEmpty(), customOAuth2User.getAuthorities().iterator().next());
            }
        }
        return null;
    }

    private static AuthenticatedInformationDto getAuthenticatedInformationDto(AuthenticatedInformationDto dto, String fullName, String avatar, int cartItemCount, boolean empty, GrantedAuthority next) {
        dto.setFullName(fullName);
        dto.setAvatar(avatar);
        dto.setCartItemCount(cartItemCount);

        if (!empty) {
            String role = next.getAuthority();
            dto.setRoleSlug(role.replace("ROLE_", "").toUpperCase());
        }

        return dto;
    }


}
