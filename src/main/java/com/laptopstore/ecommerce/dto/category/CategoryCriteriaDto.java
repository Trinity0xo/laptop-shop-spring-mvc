package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCriteriaDto extends PageableCriteriaDto {
    private String name;
}