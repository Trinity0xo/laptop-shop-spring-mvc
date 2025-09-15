package com.laptopstore.ecommerce.validation.category;

import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class UpdateCategoryValidator extends BaseCategoryValidator<UpdateCategoryDto> implements ConstraintValidator<UpdateCategoryConstraint, UpdateCategoryDto> {
    private final CategoryService categoryService;

    public UpdateCategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected MultipartFile getNewImageFile(UpdateCategoryDto dto) {
        return dto.getNewImage();
    }

    @Override
    protected String getName(UpdateCategoryDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(UpdateCategoryDto dto) {
        return dto.getDescription();
    }

    @Override
    public boolean isValid(UpdateCategoryDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value,context);

        Category exists = this.categoryService.getCategoryByName(value.getName());
        if (exists != null && !exists.getId().equals(value.getId())) {
            context.buildConstraintViolationWithTemplate("Đã tồn tại một danh mục với tên này")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }


        return isValid;
    }
}
