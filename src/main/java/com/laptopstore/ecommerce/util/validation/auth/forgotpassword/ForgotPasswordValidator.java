package com.laptopstore.ecommerce.util.validation.auth.forgotpassword;

import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.service.impl.UserServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ForgotPasswordValidator implements ConstraintValidator<ForgotPasswordConstraint, ForgotPasswordDto> {
    private final UserServiceImpl userServiceImpl;

    public ForgotPasswordValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean isValid(ForgotPasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getEmail() == null || !value.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            context.buildConstraintViolationWithTemplate("Email không hợp lệ")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        boolean exists = this.userServiceImpl.checkIfUserExistsByEmail(value.getEmail());
        if (!exists){
            context.buildConstraintViolationWithTemplate("Không tìm thấy người dùng với email này")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
