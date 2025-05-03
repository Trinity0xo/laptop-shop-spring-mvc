package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;

import com.laptopstore.ecommerce.util.anotaion.validation.product.EditProductConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@EditProductConstraint
public class UpdateProductDto {
    private Long id;
    private List<MultipartFile> images;
    private String name;
    private Double price;
    private Float discount;
    private Long quantity;
    private String description;
    private String shortDescription;
    private Category category;
    private Brand brand;
    private List<String> deleteImageNames;
}
