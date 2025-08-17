package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.dto.MonthlyCountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlySalesDto extends MonthlyCountDto {
    private double salesValue;

    public MonthlySalesDto(int month, double salesValue){
        this.month = month;
        this.salesValue = salesValue;
    }
}
