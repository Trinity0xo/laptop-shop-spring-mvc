package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.validation.user.CreateUserConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@CreateUserConstraint
public class CreateUserDto extends BaseUserInfoDto {
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
    private List<Role> roles;

    public CreateUserDto(List<Role> roles){
        this.roles = roles;
    }
}

