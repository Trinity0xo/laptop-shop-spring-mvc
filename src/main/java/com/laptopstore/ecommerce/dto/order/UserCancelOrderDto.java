package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.validation.order.UserCancelOrderConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UserCancelOrderConstraint
public class UserCancelOrderDto extends BaseOrderUpdateDto {
    public UserCancelOrderDto(Long id){
        this.id = id;
    }
}
