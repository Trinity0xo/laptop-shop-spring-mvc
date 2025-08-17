package com.laptopstore.ecommerce.util.validation.order;

import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseOrderUpdateValidator<T> {
    protected abstract String getCancelledReason(T dto);

    protected boolean validate(T dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        String cancelledReason = getCancelledReason(dto);

        if(cancelledReason == null || cancelledReason.isEmpty()){
            context.buildConstraintViolationWithTemplate("Cancel reason is required")
                    .addPropertyNode("cancelledReason")
                    .addConstraintViolation();
            isValid = false;
        }else if(cancelledReason.length() > 5000){
            context.buildConstraintViolationWithTemplate("Cancel reason must not exceed 5000 characters")
                    .addPropertyNode("cancelledReason")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
