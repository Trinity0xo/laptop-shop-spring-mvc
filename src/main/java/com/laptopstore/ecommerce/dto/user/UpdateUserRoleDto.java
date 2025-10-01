package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.validation.user.UpdateUserRoleConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@UpdateUserRoleConstraint
public class UpdateUserRoleDto {
    private Long id;
    private String email;
    private Role currentRole;
    private List<Role> roles;
    private Role newRole;

    public UpdateUserRoleDto(Long id, String email, Role currentRole, List<Role> roles){
        this.id = id;
        this.email = email;
        this.currentRole = currentRole;
        this.roles = roles;
    }
}
