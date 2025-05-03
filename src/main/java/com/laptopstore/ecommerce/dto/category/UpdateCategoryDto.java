package com.laptopstore.ecommerce.dto.category;

import com.laptopstore.ecommerce.util.anotaion.validation.category.EditCategoryConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@EditCategoryConstraint
public class UpdateCategoryDto {
    private Long id;
    private MultipartFile image;
    private String name;
    private String description;
}
