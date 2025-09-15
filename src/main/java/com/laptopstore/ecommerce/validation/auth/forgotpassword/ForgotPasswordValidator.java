package com.laptopstore.ecommerce.validation.auth.forgotpassword;

import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ForgotPasswordValidator implements ConstraintValidator<ForgotPasswordConstraint, ForgotPasswordDto> {

    @Override
    public boolean isValid(ForgotPasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getEmail() == null || !value.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            context.buildConstraintViolationWithTemplate("Email không hợp lệ")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
