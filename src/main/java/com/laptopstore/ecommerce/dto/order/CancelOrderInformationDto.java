package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.util.validation.order.UserCancelOrderConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UserCancelOrderConstraint
public class CancelOrderInformationDto extends BaseOrderUpdateDto {
    public CancelOrderInformationDto(long id){
        this.id = id;
    }
}
