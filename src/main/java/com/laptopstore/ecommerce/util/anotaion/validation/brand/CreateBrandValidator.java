package com.laptopstore.ecommerce.util.anotaion.validation.brand;

import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.service.BrandService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateBrandValidator implements ConstraintValidator<CreateBrandConstraint, CreateBrandDto> {
    private final BrandService brandService;

    public CreateBrandValidator(BrandService brandService) {
        this.brandService = brandService;
    }

    @Override
    public boolean isValid(CreateBrandDto value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if(value.getName() == null || value.getName().length() < 2){
            context.buildConstraintViolationWithTemplate("Name must be at least 2 characters")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        Brand exists = this.brandService.handleGetBrandByName(value.getName());
        if(exists != null){
            context.buildConstraintViolationWithTemplate("A brand with this name already exists")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }
}
