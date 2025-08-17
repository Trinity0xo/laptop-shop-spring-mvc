package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomProductSoldDto extends BaseProductDto {
    private long id;
    private long sold;
    private ProductImage productImage;

    public CustomProductSoldDto(long id, ProductImage productImage, String name, String slug, long sold, long quantity){
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.quantity = quantity;
        this.productImage = productImage;
        this.slug = slug;
    }
}
