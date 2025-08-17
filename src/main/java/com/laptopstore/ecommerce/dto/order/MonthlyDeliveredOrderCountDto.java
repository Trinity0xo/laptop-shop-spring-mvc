package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.dto.MonthlyCountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyDeliveredOrderCountDto extends MonthlyCountDto {
    private long orderCount;

    public MonthlyDeliveredOrderCountDto(int month, long orderCount){
        this.month = month;
        this.orderCount = orderCount;
    }
}
