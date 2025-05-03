package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.model.ResetPasswordToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String resetPasswordToken;
    private String newPassword;
    private String confirmPassword;
}
