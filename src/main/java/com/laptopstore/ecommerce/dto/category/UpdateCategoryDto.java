package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.validation.category.UpdateCategoryConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateCategoryConstraint
public class UpdateCategoryDto extends BaseCategoryDto {
    private Long id;
    private String currentImageName;
    private String oldName;

    public UpdateCategoryDto(Long id, String currentImageName, String name, String oldName, String description){
        this.id = id;
        this.currentImageName = currentImageName;
        this.oldName = oldName;
        this.name = name;
        this.description = description;
    }
}
