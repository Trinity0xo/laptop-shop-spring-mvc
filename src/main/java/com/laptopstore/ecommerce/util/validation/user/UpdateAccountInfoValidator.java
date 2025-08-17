package com.laptopstore.ecommerce.util.validation.user;

import com.laptopstore.ecommerce.dto.user.UpdateUserInformationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class UpdateAccountInfoValidator implements ConstraintValidator<UpdateAccountInfoConstraint, UpdateUserInformationDto> {
    @Override
    public boolean isValid(UpdateUserInformationDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        List<String> allowFileTypes = new ArrayList<>(List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp"));

        if (value.getFirstName() == null || value.getFirstName().length() < 2) {
            context.buildConstraintViolationWithTemplate("First name must be at least 2 characters")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getFirstName() == null || value.getLastName().length() < 2) {
            context.buildConstraintViolationWithTemplate("Last name must be at least 2 characters")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getPhone() != null && !value.getPhone().matches("0\\d{9,14}")) {
            context.buildConstraintViolationWithTemplate("Invalid phone number")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getNewAvatar() != null && !value.getNewAvatar().isEmpty() && !allowFileTypes.contains(value.getNewAvatar().getContentType())){
            context.buildConstraintViolationWithTemplate("Invalid image type")
                    .addPropertyNode("newAvatarFile")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getAddress() != null && value.getAddress().length() > 255) {
            context.buildConstraintViolationWithTemplate("Address must not exceed 255 characters")
                    .addPropertyNode("address")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
