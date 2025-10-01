package com.laptopstore.ecommerce.validation.auth.changePassword;

import com.laptopstore.ecommerce.dto.auth.ChangePasswordDto;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.service.UserService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ChangePasswordValidator implements ConstraintValidator<ChangePasswordConstraint, ChangePasswordDto> {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(ChangePasswordDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        String email = AuthenticationUtils.getAuthenticatedName();
        User user = this.userService.getUserByEmail(email);

        if (value.getOldPassword() == null || !passwordEncoder.matches(value.getOldPassword(), user.getPassword())) {
            context.buildConstraintViolationWithTemplate("Mật khẩu cũ không chính xác")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getNewPassword() == null || value.getNewPassword().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Mật khẩu mới không được để trống")
                    .addPropertyNode("newPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getConfirmNewPassword() == null || !value.getConfirmNewPassword().equals(value.getNewPassword())) {
            context.buildConstraintViolationWithTemplate("Mật khẩu xác nhận không khớp")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
