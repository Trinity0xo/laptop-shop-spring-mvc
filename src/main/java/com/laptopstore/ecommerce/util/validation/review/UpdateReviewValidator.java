package com.laptopstore.ecommerce.util.validation.review;

import com.laptopstore.ecommerce.dto.review.UpdateReviewDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateReviewValidator extends BaseReviewValidator<UpdateReviewDto> implements ConstraintValidator<UpdateReviewConstraint, UpdateReviewDto> {
    @Override
    protected Integer getRating(UpdateReviewDto dto) {
        return dto.getRating();
    }

    @Override
    protected String getMessage(UpdateReviewDto dto) {
        return dto.getMessage();
    }

    @Override
    public boolean isValid(UpdateReviewDto value, ConstraintValidatorContext context) {
        return validate(value, context);
    }
}
