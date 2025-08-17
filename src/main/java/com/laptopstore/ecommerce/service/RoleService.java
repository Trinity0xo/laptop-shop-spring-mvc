package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.role.RoleFilterDto;
import com.laptopstore.ecommerce.model.Role;

import java.util.List;

public interface RoleService {
    long countRole();
    void saveAllRoles(List<Role> roles);
    Role getRoleById(long roleId);
    PageResponse<List<Role>> getAllRoles(RoleFilterDto roleFilterDto);
}
