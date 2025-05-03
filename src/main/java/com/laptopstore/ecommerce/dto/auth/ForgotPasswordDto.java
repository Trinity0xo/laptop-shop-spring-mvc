package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.util.anotaion.validation.auth.forgotpassoword.ForgotPasswordConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ForgotPasswordConstraint
public class ForgotPasswordDto {
    private String email;
}
