package com.laptopstore.ecommerce.validation.auth.register;

import com.laptopstore.ecommerce.dto.auth.RegisterDto;

import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.validation.user.BaseCreateUserValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterValidator extends BaseCreateUserValidator<RegisterDto> implements ConstraintValidator<RegisterConstraint, RegisterDto> {
    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext context) {
        boolean isValid = true;

        User exists = this.userService.getUserByEmail(registerDto.getEmail());
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("Email này đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }

    @Override
    protected String getFirstName(RegisterDto dto) {
        return dto.getFirstName();
    }

    @Override
    protected String getLastName(RegisterDto dto) {
        return dto.getLastName();
    }

    @Override
    protected String getEmail(RegisterDto dto) {
        return dto.getEmail();
    }

    @Override
    protected String getPassword(RegisterDto dto) {
        return dto.getPassword();
    }

    @Override
    protected String getConfirmPassword(RegisterDto dto) {
        return dto.getConfirmPassword();
    }
}
