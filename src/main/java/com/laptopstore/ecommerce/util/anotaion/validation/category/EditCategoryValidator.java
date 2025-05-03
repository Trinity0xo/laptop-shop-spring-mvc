package com.laptopstore.ecommerce.util.anotaion.validation.category;

import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EditCategoryValidator implements ConstraintValidator<EditCategoryConstraint, UpdateCategoryDto> {
    private final CategoryService categoryService;

    public EditCategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean isValid(UpdateCategoryDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        List<String> allowFileTypes = new ArrayList<>(List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp"));

        if(value.getName() == null || value.getName().length() < 3){
            context.buildConstraintViolationWithTemplate("Name must be at least 3 characters")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getImage() != null && !value.getImage().isEmpty() && !allowFileTypes.contains(value.getImage().getContentType())){
            context.buildConstraintViolationWithTemplate("Invalid image type")
                    .addPropertyNode("image")
                    .addConstraintViolation();
            isValid = false;
        }

        Category exists = this.categoryService.handleGetCategoryByName(value.getName());
        if(exists != null && !exists.getId().equals(value.getId())){
            context.buildConstraintViolationWithTemplate("A category with this name already exists")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }
}
