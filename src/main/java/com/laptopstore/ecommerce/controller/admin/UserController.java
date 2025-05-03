package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.dto.user.UserCriteriaDto;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.RoleService;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.error.ConflictException;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showUserPage(
            Model model, UserCriteriaDto userCriteriaDto) {
        Page<User> users = this.userService.handleGetAllUsers(userCriteriaDto);
        model.addAttribute("userList", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", users.getPageable().getPageNumber() + 1);
        model.addAttribute("query", userCriteriaDto);
        model.addAttribute("resultCount", users.getTotalElements());

        return "/admin/user/index";
    }

//    @GetMapping("/create")
//    public String showCreateUserPage(Model model) {
//        List<Role> role = this.roleService.handleGetAllRoles();
//        model.addAttribute("registerDto", new RegisterDto());
//        model.addAttribute("roles", role);
//
//        return "/admin/user/create";
//    }

//    @PostMapping("/create")
//    public String createUser(
//            @Valid RegisterDto registerDto) {
//        User user = this.userService.handleGetUserByEmail(registerDto.getEmail());
//        if (user != null) {
//            throw new ConflictException("Account with this email already exists");
//        }
//
//        String hashedPassword = this.passwordEncoder.encode(registerDto.getPassword());
//        registerDto.setPassword(hashedPassword);
//
//        this.userService.handleCreateNewUser(registerDto);
//
//        return "redirect:/dashboard/user";
//    }

    @GetMapping("/details/{id}")
    public String showCreateUserPage(Model model, @PathVariable Long id) {
        User user = this.userService.handleGetUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        model.addAttribute("user", user);

        return "/admin/user/details";
    }
}
