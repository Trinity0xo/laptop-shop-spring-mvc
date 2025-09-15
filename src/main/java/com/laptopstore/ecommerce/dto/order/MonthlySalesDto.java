package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.dto.MonthlyCountDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthlySalesDto extends MonthlyCountDto {
    private double salesValue;

    public MonthlySalesDto(int month, double salesValue){
        this.month = month;
        this.salesValue = salesValue;
    }
}
