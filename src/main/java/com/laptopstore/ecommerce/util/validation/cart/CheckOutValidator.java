package com.laptopstore.ecommerce.util.validation.cart;

import com.laptopstore.ecommerce.dto.cart.CheckoutDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckOutValidator implements ConstraintValidator<CheckOutConstraint, CheckoutDto> {
    @Override
    public boolean isValid(CheckoutDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getFirstName() == null || value.getFirstName().length() < 2 || value.getFirstName().length() > 50) {
            context.buildConstraintViolationWithTemplate("Tên phải từ 2 đến 50 ký tự")
                    .addPropertyNode("receiverFirstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getLastName() == null || value.getLastName().length() < 2 || value.getLastName().length() > 50) {
            context.buildConstraintViolationWithTemplate("Họ phải từ 2 đến 50 ký tự")
                    .addPropertyNode("receiverLastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getEmail() == null || value.getEmail().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Email là bắt buộc")
                    .addPropertyNode("receiverEmail")
                    .addConstraintViolation();
            isValid = false;
        } else if (!value.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            context.buildConstraintViolationWithTemplate("Email không hợp lệ")
                    .addPropertyNode("receiverEmail")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getPhone() == null || !value.getPhone().matches("0\\d{9,14}")) {
            context.buildConstraintViolationWithTemplate("Số điện thoại không hợp lệ")
                    .addPropertyNode("receiverPhone")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getAddress() == null || value.getAddress().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Địa chỉ là bắt buộc")
                    .addPropertyNode("receiverAddress")
                    .addConstraintViolation();
            isValid = false;
        } else if (value.getAddress().length() > 255) {
            context.buildConstraintViolationWithTemplate("Địa chỉ không được vượt quá 255 ký tự")
                    .addPropertyNode("receiverAddress")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getNote() != null && value.getNote().length() > 5000) {
            context.buildConstraintViolationWithTemplate("Ghi chú không được vượt quá 5000 ký tự")
                    .addPropertyNode("receiverNote")
                    .addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}
