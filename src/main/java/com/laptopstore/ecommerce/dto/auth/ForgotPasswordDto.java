package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.validation.auth.forgotpassword.ForgotPasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ForgotPasswordConstraint
public class ForgotPasswordDto {
    private String email;
}
