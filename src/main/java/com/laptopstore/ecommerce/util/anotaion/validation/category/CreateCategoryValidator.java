package com.laptopstore.ecommerce.util.anotaion.validation.category;

import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class CreateCategoryValidator implements ConstraintValidator<CreateCategoryConstraint, CreateCategoryDto> {
    private final CategoryService categoryService;

    public CreateCategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean isValid(CreateCategoryDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        List<String> allowFileTypes = new ArrayList<>(List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp"));

        if(value.getName() == null || value.getName().length() < 2){
            context.buildConstraintViolationWithTemplate("Name must be at least 2 characters")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getImage() == null || value.getImage().isEmpty()){
            context.buildConstraintViolationWithTemplate("Image is required")
                    .addPropertyNode("image")
                    .addConstraintViolation();
            isValid = false;
        }else if(!allowFileTypes.contains(value.getImage().getContentType())){
            context.buildConstraintViolationWithTemplate("Invalid image type")
                    .addPropertyNode("image")
                    .addConstraintViolation();
            isValid = false;
        }

        Category exists = this.categoryService.handleGetCategoryByName(value.getName());
        if(exists != null){
            context.buildConstraintViolationWithTemplate("A category with this name already exists")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }
}
