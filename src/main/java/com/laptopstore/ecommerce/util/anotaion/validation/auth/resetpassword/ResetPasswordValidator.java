package com.laptopstore.ecommerce.util.anotaion.validation.auth.resetpassword;

import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ResetPasswordValidator implements ConstraintValidator<ResetPasswordConstraint, ResetPasswordDto> {
    @Override
    public boolean isValid(ResetPasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (value.getNewPassword() == null || value.getNewPassword().isEmpty()) {
            context.buildConstraintViolationWithTemplate("New password cannot be empty")
                    .addPropertyNode("newPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getConfirmPassword() == null || !value.getConfirmPassword().equals(value.getNewPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
