package com.laptopstore.ecommerce.util.validation.user;

import com.laptopstore.ecommerce.dto.user.CreateUserDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.EnumUtils;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateUserValidator extends BaseCreateUserValidator<CreateUserDto> implements ConstraintValidator<CreateUserConstraint, CreateUserDto> {
    private final UserService userService;

    public CreateUserValidator(UserService userService, RoleService roleService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(CreateUserDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value, context);

        User exists = this.userService.getUserByEmail(value.getEmail());
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("Email này đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation();

            isValid = false;
        }

        if(value.getRole() == null || !EnumUtils.isInEnum(value.getRole(), RoleEnum.class)){
                context.buildConstraintViolationWithTemplate("Vui lòng chọn vai trò")
                    .addPropertyNode("role")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }

    @Override
    protected String getFirstName(CreateUserDto dto) {
        return dto.getFirstName();
    }

    @Override
    protected String getLastName(CreateUserDto dto) {
        return dto.getLastName();
    }

    @Override
    protected String getEmail(CreateUserDto dto) {
        return dto.getEmail();
    }

    @Override
    protected String getPassword(CreateUserDto dto) {
        return dto.getPassword();
    }

    @Override
    protected String getConfirmPassword(CreateUserDto dto) {
        return dto.getConfirmPassword();
    }
}
