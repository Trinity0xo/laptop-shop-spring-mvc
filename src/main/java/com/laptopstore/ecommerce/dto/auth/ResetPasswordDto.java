package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.validation.auth.resetpassword.ResetPasswordConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ResetPasswordConstraint
public class ResetPasswordDto {
    private String resetPasswordToken;
    private String newPassword;
    private String confirmNewPassword;

    public ResetPasswordDto(String resetPasswordToken){
        this.resetPasswordToken = resetPasswordToken;
    }
}
