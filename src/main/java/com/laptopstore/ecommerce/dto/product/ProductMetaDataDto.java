package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductMetaDataDto {
    List<Category> categories;
    List<Brand> brands;

    public ProductMetaDataDto(List<Category> categories, List<Brand> brands){
        this.categories = categories;
        this.brands = brands;
    }
}
