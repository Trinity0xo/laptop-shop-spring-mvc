package com.laptopstore.ecommerce.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDto {
    private String resetPasswordToken;
    private String newPassword;
    private String confirmPassword;

    public ResetPasswordDto(String resetPasswordToken){
        this.resetPasswordToken = resetPasswordToken;
    }
}
