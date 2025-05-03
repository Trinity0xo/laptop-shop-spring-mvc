package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.util.anotaion.validation.category.CreateCategoryConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@CreateCategoryConstraint
public class CreateCategoryDto {
    private String name;
    private String description;
    private MultipartFile image;
}
