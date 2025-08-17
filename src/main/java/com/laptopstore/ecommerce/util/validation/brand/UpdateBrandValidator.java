package com.laptopstore.ecommerce.util.validation.brand;

import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.service.impl.BrandServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateBrandValidator extends BaseBrandValidator<UpdateBrandDto> implements ConstraintValidator<UpdateBrandConstraint, UpdateBrandDto> {
    private final BrandServiceImpl brandServiceImpl;

    public UpdateBrandValidator(BrandServiceImpl brandServiceImpl) {
        this.brandServiceImpl = brandServiceImpl;
    }

    @Override
    protected String getName(UpdateBrandDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(UpdateBrandDto dto) {
        return dto.getName();
    }

    @Override
    public boolean isValid(UpdateBrandDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value, context);

        Brand exists = this.brandServiceImpl.getBrandByName(value.getName());
        if (exists != null && !exists.getId().equals(value.getId())) {
            context.buildConstraintViolationWithTemplate("Thương hiệu với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }


        return isValid;
    }
}
