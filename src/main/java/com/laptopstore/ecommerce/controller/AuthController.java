package com.laptopstore.ecommerce.controller;

import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import com.laptopstore.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid RegisterDto registerDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/auth/register";
        }

        this.userService.registerAccount(registerDto);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công");
        return "redirect:/auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordDto", new ForgotPasswordDto());
        return "/auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid ForgotPasswordDto forgotPasswordDto,
            BindingResult bindingResult,
            Model model

    )  {
        if (bindingResult.hasErrors()) {
            return "/auth/forgot_password";
        }

        String email = this.userService.forgotPassword(forgotPasswordDto);
        model.addAttribute("email", email);

        return "/auth/forgot_password_confirmation";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam(value = "token", defaultValue = "") String token,
            Model model
    ) {
        ResetPasswordDto resetPasswordInformation = this.userService.getResetPasswordInformation(token);
        model.addAttribute("resetPasswordInformation", resetPasswordInformation);

        return "/auth/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid ResetPasswordDto resetPasswordInformation,
            BindingResult bindingResult
    )  {
        if (bindingResult.hasErrors()) {
            return "/auth/reset_password";
        }
        this.userService.resetPassword(resetPasswordInformation);

        return "/auth/reset_password_confirmation";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        Object errorMessage = session.getAttribute("loginErrorMessage");
        if (errorMessage != null) {
            model.addAttribute("loginErrorMessage", errorMessage);
            session.removeAttribute("loginErrorMessage");
        }

        return "/auth/login";
    }
}
