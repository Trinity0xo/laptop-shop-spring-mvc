package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.util.anotaion.validation.brand.EditBrandConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EditBrandConstraint
public class UpdateBrandDto {
    private Long id;
    private String name;
    private String description;
}
