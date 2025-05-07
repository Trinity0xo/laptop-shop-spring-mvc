package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.role.RoleCriteriaDto;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showRolePage(
            RoleCriteriaDto roleCriteriaDto,
            Model model
    ) throws Exception {
        Page<Role> roles = this.roleService.handleGetAllRoles(roleCriteriaDto);
        model.addAttribute("roleList", roles.getContent());
        model.addAttribute("totalPages", roles.getTotalPages());
        model.addAttribute("currentPage", roles.getPageable().getPageNumber() + 1);
        model.addAttribute("query", roleCriteriaDto);
        model.addAttribute("resultCount", roles.getTotalElements());

        return "/admin/role/index";
    }

    @GetMapping("/details/{id}")
    public String showRoleDetailsPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Role role = this.roleService.handleGetRoleById(id);
        if (role == null) {
            throw new NotFoundException("Role not found");
        }

        model.addAttribute("role", role);

        return "/admin/role/details";
    }
}
