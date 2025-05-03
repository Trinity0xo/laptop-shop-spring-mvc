package com.laptopstore.ecommerce.util.anotaion.validation.product;

import com.laptopstore.ecommerce.dto.product.CreateProductDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class CreateProductValidator implements ConstraintValidator<CreateProductConstraint, CreateProductDto> {
    @Override
    public boolean isValid(CreateProductDto value, ConstraintValidatorContext context) {
        boolean isValid = true;

        List<String> allowFileTypes = new ArrayList<>(List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp"));

        if(value.getName() == null || value.getName().length() < 2) {
            context.buildConstraintViolationWithTemplate("Name must be at least 2 characters")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getPrice() == null || value.getPrice() < 0){
            context.buildConstraintViolationWithTemplate("Price must be greater than 0")
                    .addPropertyNode("price")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getQuantity() == null || value.getQuantity() < 0){
            context.buildConstraintViolationWithTemplate("Quantity must be greater or equal 0")
                    .addPropertyNode("quantity")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getDiscount() == null || value.getDiscount() < 0 || value.getDiscount() > 100){
            context.buildConstraintViolationWithTemplate("Discount must be between 0 and 100")
                    .addPropertyNode("discount")
                    .addConstraintViolation();
            isValid = false;
        }

        if(value.getImages() == null || value.getImages().isEmpty()){
            context.buildConstraintViolationWithTemplate("Images must not be empty")
                    .addPropertyNode("images")
                    .addConstraintViolation();
            isValid = false;
        }else {
            for (MultipartFile image : value.getImages()) {
                if(!allowFileTypes.contains(image.getContentType())){
                    context.buildConstraintViolationWithTemplate("Invalid image type")
                            .addPropertyNode("images")
                            .addConstraintViolation();
                    isValid = false;
                    break;
                }
            }
        }

        return isValid;
    }
}
