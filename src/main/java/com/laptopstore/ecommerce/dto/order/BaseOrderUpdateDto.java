package com.laptopstore.ecommerce.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseOrderUpdateDto {
    protected Long id;
    protected String cancelledReason;
}
