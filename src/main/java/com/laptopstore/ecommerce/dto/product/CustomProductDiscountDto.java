package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomProductDiscountDto extends BaseProductDto {
    private long id;
    private ProductImage productImage;

    public CustomProductDiscountDto(Long id, ProductImage productImage, String name, String slug, Float discount, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.productImage = productImage;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static final String DEFAULT_SORT_FIELD = "updatedAt";

    public static final List<String> VALID_SORT_FIELDS = List.of("id", "name", "discount", "createdAt", "updatedAt");
}
