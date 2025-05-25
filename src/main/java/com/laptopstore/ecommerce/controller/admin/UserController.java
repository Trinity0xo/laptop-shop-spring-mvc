package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.user.UserCriteriaDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUserPage(
            Model model,
            UserCriteriaDto userCriteriaDto
    ) throws Exception {
        Page<User> users = this.userService.handleGetAllUsers(userCriteriaDto);
        model.addAttribute("userList", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", users.getPageable().getPageNumber() + 1);
        model.addAttribute("query", userCriteriaDto);
        model.addAttribute("resultCount", users.getTotalElements());

        return "/admin/user/index";
    }

    @GetMapping("/details/{id}")
    public String showCreateUserPage(
            Model model,
            @PathVariable Long id
    ) throws Exception {
        User user = this.userService.handleGetUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        model.addAttribute("user", user);

        return "/admin/user/details";
    }
}
