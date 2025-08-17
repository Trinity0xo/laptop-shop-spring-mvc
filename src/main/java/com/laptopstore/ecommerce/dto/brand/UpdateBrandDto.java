package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.util.validation.brand.UpdateBrandConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateBrandConstraint
public class UpdateBrandDto extends BaseBrandDto {
    private Long id;

    public UpdateBrandDto(long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
