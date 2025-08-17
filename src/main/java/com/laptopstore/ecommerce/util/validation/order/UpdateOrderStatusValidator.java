package com.laptopstore.ecommerce.util.validation.order;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.util.EnumUtils;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateOrderStatusValidator extends BaseOrderUpdateValidator<UpdateOrderStatusDto> implements ConstraintValidator<UpdateOrderStatusConstraint, UpdateOrderStatusDto> {
    @Override
    public boolean isValid(UpdateOrderStatusDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value,context);

        if(!EnumUtils.isInEnum(value.getStatus(), OrderStatusEnum.class)){
            context.buildConstraintViolationWithTemplate("Invalid order status")
                    .addPropertyNode("orderStatus")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected String getCancelledReason(UpdateOrderStatusDto dto) {
        return dto.getCancelledReason();
    }
}
