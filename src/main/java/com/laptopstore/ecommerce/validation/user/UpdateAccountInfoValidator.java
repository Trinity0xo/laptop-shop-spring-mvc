package com.laptopstore.ecommerce.validation.user;

import com.laptopstore.ecommerce.dto.user.UpdateUserInformationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.laptopstore.ecommerce.util.ImageUtils.isValidImageType;

public class UpdateAccountInfoValidator implements ConstraintValidator<UpdateAccountInfoConstraint, UpdateUserInformationDto> {
    @Override
    public boolean isValid(UpdateUserInformationDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getFirstName() == null || value.getFirstName().length() < 2 || value.getFirstName().length() > 50) {
            context.buildConstraintViolationWithTemplate("Tên phải có độ dài từ 2 đến 50 ký tự")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getFirstName() == null || value.getLastName().length() < 2 || value.getFirstName().length() > 50) {
            context.buildConstraintViolationWithTemplate("Họ phải có độ dài từ 2 đến 50 ký tự")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getPhone() != null && !value.getPhone().matches("0\\d{9,14}")) {
            context.buildConstraintViolationWithTemplate("Số điện thoại không hợp lệ")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getNewAvatar() != null && !value.getNewAvatar().isEmpty()){
            if(!isValidImageType(value.getNewAvatar().getContentType())){
                context.buildConstraintViolationWithTemplate("File hình ảnh không hợp lệ")
                        .addPropertyNode("newAvatarFile")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        if (value.getAddress() != null && value.getAddress().length() > 255) {
            context.buildConstraintViolationWithTemplate("Địa chỉ không được vượt quá 255 ký tự")
                    .addPropertyNode("address")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
