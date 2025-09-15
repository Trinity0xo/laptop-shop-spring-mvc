package com.laptopstore.ecommerce.validation.order;

import com.laptopstore.ecommerce.dto.order.UserCancelOrderDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserCancelOrderValidator implements ConstraintValidator<UserCancelOrderConstraint, UserCancelOrderDto> {
    @Override
    public boolean isValid(UserCancelOrderDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if(value.getCancelledReason() == null || value.getCancelledReason().isEmpty()){
            context.buildConstraintViolationWithTemplate("Lý do huỷ đơn là bắt buộc")
                    .addPropertyNode("cancelledReason")
                    .addConstraintViolation();
            isValid = false;
        } else if(value.getCancelledReason().length() > 5000){
            context.buildConstraintViolationWithTemplate("Lý do huỷ đơn không được vượt quá 5000 ký tự")
                    .addPropertyNode("cancelledReason")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
