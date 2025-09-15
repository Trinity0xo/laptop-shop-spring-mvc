package com.laptopstore.ecommerce.validation.review;

import com.laptopstore.ecommerce.dto.review.CreateReviewDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateReviewValidator extends BaseReviewValidator<CreateReviewDto> implements ConstraintValidator<CreateReviewConstraint, CreateReviewDto> {
    @Override
    protected Integer getRating(CreateReviewDto dto) {
        return dto.getRating();
    }

    @Override
    protected String getMessage(CreateReviewDto dto) {
        return dto.getMessage();
    }

    @Override
    public boolean isValid(CreateReviewDto value, ConstraintValidatorContext context) {
        return validate(value, context);
    }
}
