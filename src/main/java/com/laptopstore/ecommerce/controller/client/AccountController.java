package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.user.UpdateAccountDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showAccountPage(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setFirstName(user.getFirstName());
        updateAccountDto.setLastName(user.getLastName());
        updateAccountDto.setPhone(user.getPhone());
        updateAccountDto.setAddress(user.getAddress());

        model.addAttribute("updateAccountDto", updateAccountDto);

        return "/client/account";
    }

    @PostMapping("/update")
    public String updateAccountInfo(
            @Valid UpdateAccountDto updateAccountDto,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "/client/account";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.handleGetUserByEmail(email);

        this.userService.handleUpdateAccountInfo(user, updateAccountDto);

        String successMessage = "Account updated successfully";

        return "redirect:/account?successMessage=" + successMessage;
    }
}
