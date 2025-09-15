package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.validation.user.CreateUserConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@CreateUserConstraint
public class CreateUserDto extends BaseUserInfoDto {
    private String email;
    private String password;
    private String confirmPassword;
    private RoleEnum role;
}

