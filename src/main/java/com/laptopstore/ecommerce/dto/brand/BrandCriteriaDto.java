package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandCriteriaDto extends PageableCriteriaDto {
    private String name;
}
