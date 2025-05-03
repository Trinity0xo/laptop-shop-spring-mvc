package com.laptopstore.ecommerce.util.anotaion.validation.auth.forgotpassoword;

import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ForgotPasswordValidator implements ConstraintValidator<ForgotPasswordConstraint, ForgotPasswordDto> {
    private final UserService userService;

    public ForgotPasswordValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(ForgotPasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getEmail() == null || !value.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            context.buildConstraintViolationWithTemplate("Invalid email address")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        boolean exists = this.userService.handleCheckIfUserExistsByEmail(value.getEmail());
        if (!exists){
            context.buildConstraintViolationWithTemplate("Could not find user with this email")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
