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
public class CustomProductStockDto extends BaseProductDto {
    private long id;
    private ProductImage productImage;

    public CustomProductStockDto(long id, ProductImage productImage, String name, String slug, long quantity, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.productImage = productImage;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static final String DEFAULT_SORT_FIELD = "quantity";

    public static final List<String> VALID_SORT_FIELDS = List.of("id", "name", "quantity", "createdAt", "updatedAt");
}
