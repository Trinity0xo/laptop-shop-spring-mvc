package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.util.validation.user.CreateUserConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CreateUserConstraint
public class CreateUserDto extends BaseUserInfoDto {
    private String email;
    private String password;
    private String confirmPassword;
    private RoleEnum role;
}

