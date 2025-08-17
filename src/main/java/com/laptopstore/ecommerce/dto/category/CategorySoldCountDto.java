package com.laptopstore.ecommerce.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategorySoldCountDto {
    private String categoryName;
    private long soldCount;
}
