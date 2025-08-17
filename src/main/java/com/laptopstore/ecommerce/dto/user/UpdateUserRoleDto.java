package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.util.validation.user.UpdateUserRoleConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UpdateUserRoleConstraint
public class UpdateUserRoleDto {
    private Long id;
    private String email;
    private RoleEnum role;

    public UpdateUserRoleDto(long id, String email, RoleEnum role){
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
