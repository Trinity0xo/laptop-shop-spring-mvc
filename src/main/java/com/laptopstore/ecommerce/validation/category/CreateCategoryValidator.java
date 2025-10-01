package com.laptopstore.ecommerce.validation.category;

import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.CategoryService;
import com.laptopstore.ecommerce.util.SlugUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class CreateCategoryValidator extends BaseCategoryValidator<CreateCategoryDto> implements ConstraintValidator<CreateCategoryConstraint, CreateCategoryDto> {
    private final CategoryService categoryService;

    public CreateCategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected MultipartFile getNewImageFile(CreateCategoryDto dto) {
        return dto.getNewImage();
    }

    @Override
    protected String getName(CreateCategoryDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(CreateCategoryDto dto) {
        return dto.getDescription();
    }

    @Override
    public boolean isValid(CreateCategoryDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value, context);

        if (value.getNewImage() == null || value.getNewImage().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Hình ảnh là bắt buộc")
                    .addPropertyNode("newImage")
                    .addConstraintViolation();
            isValid = false;
        }

        Category exists = this.categoryService.getCategoryBySlug(SlugUtils.toSlug(value.getName()));
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("Danh mục với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}
