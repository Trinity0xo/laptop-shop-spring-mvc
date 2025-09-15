package com.laptopstore.ecommerce.dto;

import com.laptopstore.ecommerce.dto.product.CustomProductDto;
import com.laptopstore.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeContentDto {
    private List<CustomProductDto> topDiscountProducts;
    private List<Category> categories;
}
