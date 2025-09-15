package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.validation.user.UpdateUserRoleConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateUserRoleConstraint
public class UpdateUserRoleDto {
    private Long id;
    private String email;
    private RoleEnum role;

    public UpdateUserRoleDto(Long id, String email, RoleEnum role){
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
