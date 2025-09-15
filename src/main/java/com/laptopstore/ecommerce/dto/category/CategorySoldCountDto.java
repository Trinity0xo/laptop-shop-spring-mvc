package com.laptopstore.ecommerce.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySoldCountDto {
    private String categoryName;
    private long soldCount;
}
