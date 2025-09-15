package com.laptopstore.ecommerce.validation.category;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static com.laptopstore.ecommerce.util.ImageUtils.isValidImageType;

public abstract class BaseCategoryValidator<T> {
    protected abstract MultipartFile getNewImageFile(T dto);
    protected abstract String getName(T dto);
    protected abstract String getDescription(T dto);

    protected boolean validate(T dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        MultipartFile newImageFile = getNewImageFile(dto);
        String name = getName(dto);
        String description = getDescription(dto);

        if (newImageFile != null && !newImageFile.isEmpty()) {
            if (!isValidImageType(newImageFile.getContentType())) {
                context.buildConstraintViolationWithTemplate("File hình ảnh không hợp lệ")
                        .addPropertyNode("newImage")
                        .addConstraintViolation();
                isValid = false;
            }
        }

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
