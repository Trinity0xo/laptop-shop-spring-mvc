package com.laptopstore.ecommerce.util.validation.user;

import com.laptopstore.ecommerce.dto.user.UpdateUserRoleDto;
import com.laptopstore.ecommerce.util.EnumUtils;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateUserRoleValidator implements ConstraintValidator<UpdateUserRoleConstraint, UpdateUserRoleDto> {
    @Override
    public boolean isValid(UpdateUserRoleDto value, ConstraintValidatorContext context) {
        if(value.getRole() == null || !EnumUtils.isInEnum(value.getRole(), RoleEnum.class)){
            context.buildConstraintViolationWithTemplate("Vui lòng chọn vai trò")
                    .addPropertyNode("role")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
