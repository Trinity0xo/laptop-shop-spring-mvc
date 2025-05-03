package com.laptopstore.ecommerce.util.anotaion.validation.auth.register;

import com.laptopstore.ecommerce.dto.auth.RegisterDto;

import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterValidator implements ConstraintValidator<RegisterConstraint, RegisterDto> {
    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (registerDto.getFirstName() == null || registerDto.getFirstName().length() < 2) {
            context.buildConstraintViolationWithTemplate("First name must be at least 2 characters")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (registerDto.getFirstName() == null || registerDto.getLastName().length() < 2) {
            context.buildConstraintViolationWithTemplate("Last name must be at least 2 characters")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (registerDto.getFirstName() == null || !registerDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            context.buildConstraintViolationWithTemplate("Invalid email address")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        User exists = this.userService.handleGetUserByEmail(registerDto.getEmail());
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("This email already exists")
                    .addPropertyNode("email")
                    .addConstraintViolation();

            isValid = false;
        }

        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Password must not be empty")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!registerDto.getConfirmPassword().equals(registerDto.getPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

}
