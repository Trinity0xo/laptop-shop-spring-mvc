package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.dto.MonthlyCountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlySoldProductCountDto extends MonthlyCountDto {
    private long productCount;

    public MonthlySoldProductCountDto(int month, long productCount){
        this.month = month;
        this.productCount = productCount;
    }
}
