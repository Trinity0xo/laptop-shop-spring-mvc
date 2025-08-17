package com.laptopstore.ecommerce.util.validation.brand;

import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.service.BrandService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateBrandValidator extends BaseBrandValidator<CreateBrandDto> implements ConstraintValidator<CreateBrandConstraint, CreateBrandDto> {
    private final BrandService brandService;

    public CreateBrandValidator(BrandService brandService) {
        this.brandService = brandService;
    }

    @Override
    protected String getName(CreateBrandDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(CreateBrandDto dto) {
        return dto.getDescription();
    }

    @Override
    public boolean isValid(CreateBrandDto createBrandDto, ConstraintValidatorContext context) {
        boolean isValid = validate(createBrandDto, context);

        Brand exists = this.brandService.getBrandByName(createBrandDto.getName());
        if (exists != null) {
            context.buildConstraintViolationWithTemplate("Thương hiệu với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }
}
