package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

import com.laptopstore.ecommerce.validation.order.UpdateOrderStatusConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateOrderStatusConstraint
public class UpdateOrderStatusDto extends BaseOrderUpdateDto {
    private OrderStatusEnum status;

    public UpdateOrderStatusDto(Long id, OrderStatusEnum status, String cancelledReason){
        this.id = id;
        this.status = status;
        this.cancelledReason = cancelledReason;
    }
}
