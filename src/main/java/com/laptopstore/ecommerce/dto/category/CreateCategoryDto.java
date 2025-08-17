package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.util.validation.category.CreateCategoryConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CreateCategoryConstraint
public class CreateCategoryDto extends BaseCategoryDto {
}
