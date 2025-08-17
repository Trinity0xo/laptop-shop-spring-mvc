package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.util.validation.category.UpdateCategoryConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UpdateCategoryConstraint
public class UpdateCategoryDto extends BaseCategoryDto {
    private Long id;
    private String currentImageName;

    public UpdateCategoryDto(long id, String currentImageName, String name, String description){
        this.id = id;
        this.currentImageName = currentImageName;
        this.name = name;
        this.description = description;
    }
}
