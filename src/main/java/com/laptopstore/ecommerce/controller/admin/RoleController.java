package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.role.RoleFilterDto;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showRolePage(
            RoleFilterDto roleFilterDto,
            Model model
    )  {
        PageResponse<List<Role>> response = this.roleService.getAllRoles(roleFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("roleCriteriaDto", roleFilterDto);

        return "/admin/role/index";
    }

    @GetMapping("/details/{roleId}")
    public String showRoleDetailsPage(
            @PathVariable Long roleId,
            Model model
    )  {
        Role role = this.roleService.getRoleById(roleId);
        model.addAttribute("role", role);

        return "/admin/role/details";
    }
}
