package com.laptopstore.ecommerce.validation.auth.resetpassword;

import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ResetPasswordValidator implements ConstraintValidator<ResetPasswordConstraint, ResetPasswordDto> {
    @Override
    public boolean isValid(ResetPasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (value.getNewPassword() == null || value.getNewPassword().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Mật khẩu mới không được để trống")
                    .addPropertyNode("newPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getConfirmNewPassword() == null || !value.getConfirmNewPassword().equals(value.getNewPassword())) {
            context.buildConstraintViolationWithTemplate("Mật khẩu xác nhận không khớp")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
