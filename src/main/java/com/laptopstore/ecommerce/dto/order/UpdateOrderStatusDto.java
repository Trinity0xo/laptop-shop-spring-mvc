package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

import com.laptopstore.ecommerce.util.validation.order.UpdateOrderStatusConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UpdateOrderStatusConstraint
public class UpdateOrderStatusDto extends BaseOrderUpdateDto {
    private OrderStatusEnum status;

    public UpdateOrderStatusDto(long id, OrderStatusEnum status, String cancelledReason){
        this.id = id;
        this.status = status;
        this.cancelledReason = cancelledReason;
    }
}
