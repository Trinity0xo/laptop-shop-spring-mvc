package com.laptopstore.ecommerce.util.validation.category;

import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.impl.CategoryServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class CreateCategoryValidator extends BaseCategoryValidator<CreateCategoryDto> implements ConstraintValidator<CreateCategoryConstraint, CreateCategoryDto> {
    private final CategoryServiceImpl categoryServiceImpl;

    public CreateCategoryValidator(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
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

        Category exists = this.categoryServiceImpl.getCategoryByName(value.getName());
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("Danh mục với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}
