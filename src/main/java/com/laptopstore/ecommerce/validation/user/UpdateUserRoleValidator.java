package com.laptopstore.ecommerce.validation.user;

import com.laptopstore.ecommerce.dto.user.UpdateUserRoleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateUserRoleValidator implements ConstraintValidator<UpdateUserRoleConstraint, UpdateUserRoleDto> {
    @Override
    public boolean isValid(UpdateUserRoleDto value, ConstraintValidatorContext context) {
        if(value.getNewRole() == null){
            context.buildConstraintViolationWithTemplate("Vui lòng chọn vai trò")
                    .addPropertyNode("newRole")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
