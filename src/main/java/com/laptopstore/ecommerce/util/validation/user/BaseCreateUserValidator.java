package com.laptopstore.ecommerce.util.validation.user;
import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseCreateUserValidator<T> {
    protected abstract String getFirstName(T dto);
    protected abstract String getLastName(T dto);
    protected abstract String getEmail(T dto);
    protected abstract String getPassword(T dto);
    protected abstract String getConfirmPassword(T dto);

    protected boolean validate(T dto, ConstraintValidatorContext context){
        boolean isValid = true;
        String firstName = getFirstName(dto);
        String lastName = getLastName(dto);
        String email = getEmail(dto);
        String password = getPassword(dto);
        String confirmPassword = getConfirmPassword(dto);

        if (firstName == null || firstName.length() < 2) {
            context.buildConstraintViolationWithTemplate("Tên phải có ít nhất 2 ký tụ")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (lastName == null || lastName.length() < 2) {
            context.buildConstraintViolationWithTemplate("Họ phải ít nhất 2 ký tự")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            context.buildConstraintViolationWithTemplate("Email không hợp lệ")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        if (password == null || password.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Mật khẩu không được bỏ trống")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!confirmPassword.equals(password)) {
            context.buildConstraintViolationWithTemplate("Xác nhận mật khẩu không trùng")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
