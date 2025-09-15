package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomProductListDto<T> extends ProductMetaDataDto {
    List<T> customProducts;

    public CustomProductListDto(List<Category> categories, List<Brand> brands, List<T> customProducts) {
        super(categories, brands);
        this.customProducts = customProducts;
    }
}
