package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.util.anotaion.validation.brand.CreateBrandConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CreateBrandConstraint
public class CreateBrandDto {
    private String name;
    private String description;
}
