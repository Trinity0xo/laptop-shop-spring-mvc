package com.laptopstore.ecommerce.validation.order;

import com.laptopstore.ecommerce.dto.order.UpdateOrderStatusDto;
import com.laptopstore.ecommerce.util.EnumUtils;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateOrderStatusValidator implements ConstraintValidator<UpdateOrderStatusConstraint, UpdateOrderStatusDto> {
    @Override
    public boolean isValid(UpdateOrderStatusDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if(value.getStatus() == null || !EnumUtils.isInEnum(value.getStatus(), OrderStatusEnum.class)){
            context.buildConstraintViolationWithTemplate("Trạng thái đơn hàng không hợp lệ")
                    .addPropertyNode("status")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getStatus().equals(OrderStatusEnum.CANCELLED)){
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
        }

        return isValid;
    }
}
