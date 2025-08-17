package com.laptopstore.ecommerce.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticatedInformationDto {
    private String avatar;
    private String fullName;
    private String email;
    private String roleName;
    private String roleSlug;
    private int cartItemCount;
}
