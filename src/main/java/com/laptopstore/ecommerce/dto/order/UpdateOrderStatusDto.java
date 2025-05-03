package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusDto {
    private Long id;
    private OrderStatusEnum orderStatus;
    private String cancelledReason;
}
