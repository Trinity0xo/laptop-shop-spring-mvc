package com.laptopstore.ecommerce.dto.auth;

import com.laptopstore.ecommerce.validation.auth.changePassword.ChangePasswordConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ChangePasswordConstraint
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
