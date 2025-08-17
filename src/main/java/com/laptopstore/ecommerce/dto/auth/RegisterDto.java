package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.util.validation.auth.register.RegisterConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterConstraint
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
