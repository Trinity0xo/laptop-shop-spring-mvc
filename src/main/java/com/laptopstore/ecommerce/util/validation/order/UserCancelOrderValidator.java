package com.laptopstore.ecommerce.util.validation.order;

import com.laptopstore.ecommerce.dto.order.CancelOrderInformationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserCancelOrderValidator extends BaseOrderUpdateValidator<CancelOrderInformationDto> implements ConstraintValidator<UserCancelOrderConstraint, CancelOrderInformationDto> {
    @Override
    public boolean isValid(CancelOrderInformationDto value, ConstraintValidatorContext context) {
        return validate(value, context);
    }

    @Override
    protected String getCancelledReason(CancelOrderInformationDto dto) {
        return dto.getCancelledReason();
    }
}
