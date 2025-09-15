package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.validation.brand.UpdateBrandConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateBrandConstraint
public class UpdateBrandDto extends BaseBrandDto {
    private Long id;
    private String oldName;

    public UpdateBrandDto(Long id, String name, String oldName, String description){
        this.id = id;
        this.oldName = oldName;
        this.name = name;
        this.description = description;
    }
}
