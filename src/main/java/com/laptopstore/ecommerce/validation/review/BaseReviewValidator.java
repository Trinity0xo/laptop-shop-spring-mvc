package com.laptopstore.ecommerce.validation.review;

import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseReviewValidator<T> {
    protected abstract Integer getRating(T dto);
    protected abstract String getMessage(T dto);

    protected boolean validate(T dto, ConstraintValidatorContext context){
        boolean isValid = true;
        String message = getMessage(dto);
        Integer rating = getRating(dto);


        if (rating == null || rating < 1 || rating > 5) {
            context.buildConstraintViolationWithTemplate("Đánh giá phải nằm trong khoảng từ 1 đến 5")
                    .addPropertyNode("rating")
                    .addConstraintViolation();
            isValid = false;
        }

        if (message == null || message.length() < 10 || message.length() > 500) {
            context.buildConstraintViolationWithTemplate("Nội dung đánh giá phải từ 10 đến 500 ký tự")
                    .addPropertyNode("message")
                    .addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}
