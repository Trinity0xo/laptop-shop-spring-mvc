package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.validation.category.CreateCategoryConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@CreateCategoryConstraint
public class CreateCategoryDto extends BaseCategoryDto {
}
