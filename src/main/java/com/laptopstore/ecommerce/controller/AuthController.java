package com.laptopstore.ecommerce.controller;

import com.laptopstore.ecommerce.dto.auth.ForgotPasswordDto;
import com.laptopstore.ecommerce.dto.auth.RegisterDto;
import com.laptopstore.ecommerce.dto.auth.ResetPasswordDto;
import com.laptopstore.ecommerce.model.ResetPasswordToken;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.MailService;
import com.laptopstore.ecommerce.service.ResetPasswordTokenService;
import com.laptopstore.ecommerce.service.UserService;

import com.laptopstore.ecommerce.util.error.BadRequestException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final MailService mailService;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, ResetPasswordTokenService resetPasswordTokenService, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.mailService = mailService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid RegisterDto registerDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/auth/register";
        }

        String hashedPassword = this.passwordEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(hashedPassword);

        this.userService.handleCreateNewUser(registerDto);

        String successMessage = "User has been registered successfully";

        return "redirect:/auth/login?successMessage=" + successMessage;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordDto", new ForgotPasswordDto());
        return "/auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid ForgotPasswordDto forgotPasswordDto,
            BindingResult bindingResult

    ) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/auth/forgot_password";
        }

        User user = this.userService.handleGetUserByEmail(forgotPasswordDto.getEmail());

        if (user == null) {
            throw new BadRequestException("User not found");
        }

        String resetPasswordToken = this.resetPasswordTokenService.handleCreateResetPasswordToken(user);

        String fullName = user.getFirstName() + " " + user.getLastName();

        this.mailService.handleSendResetPasswordLink(fullName, user.getEmail(), resetPasswordToken);

        String successMessage = "Reset password has been sent";

        return "redirect:/shop?successMessage=" + successMessage;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam(value = "token", defaultValue = "") String token,
            Model model
    ) throws Exception{
        ResetPasswordToken resetPasswordToken = this.resetPasswordTokenService.handleGetResetPasswordToken(token);
        if(resetPasswordToken == null || resetPasswordToken.isExpired()) {
            throw new BadRequestException("Reset password link expired or invalid");
        }

        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setResetPasswordToken(resetPasswordToken.getToken());

        model.addAttribute("resetPasswordDto", resetPasswordDto);

        return "/auth/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid ResetPasswordDto resetPasswordDto,
            BindingResult bindingResult
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/auth/reset_password";
        }

        ResetPasswordToken resetPasswordToken = this.resetPasswordTokenService.handleGetResetPasswordToken(resetPasswordDto.getResetPasswordToken());
        if(resetPasswordToken == null || resetPasswordToken.isExpired()) {
            throw new BadRequestException("Reset password link expired or invalid");
        }

        User user = resetPasswordToken.getUser();

        if(user == null){
            throw new BadRequestException("User not found");
        }

        String newHashedPassword = this.passwordEncoder.encode(resetPasswordDto.getNewPassword());

        resetPasswordDto.setNewPassword(newHashedPassword);

        this.userService.handleUpdatePassword(resetPasswordDto, user);

        String successMessage = "Reset password successfully";

        return "redirect:/shop?successMessage=" + successMessage;
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
