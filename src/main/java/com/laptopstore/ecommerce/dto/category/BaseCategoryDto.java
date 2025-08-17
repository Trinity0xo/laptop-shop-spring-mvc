package com.laptopstore.ecommerce.dto.category;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public abstract class BaseCategoryDto {
    protected MultipartFile newImage;
    protected String name;
    protected String description;
}
