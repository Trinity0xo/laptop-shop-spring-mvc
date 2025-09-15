package com.laptopstore.ecommerce.validation.brand;
import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseBrandValidator<T> {
    protected abstract String getName(T dto);
    protected abstract String getDescription(T dto);
    protected boolean validate(T dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        String name = getName(dto);
        String description = getDescription(dto);

        if (name == null || name.length() < 2 || name.length() > 255) {
            context.buildConstraintViolationWithTemplate("Tên phải có độ dài từ 2 đến 255 ký tự")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if (description != null && description.length() > 5000) {
            context.buildConstraintViolationWithTemplate("Mô tả không được vượt quá 5000 ký tự")
                    .addPropertyNode("description")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
