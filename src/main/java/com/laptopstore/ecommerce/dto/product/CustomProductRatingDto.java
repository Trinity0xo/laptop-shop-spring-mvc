package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomProductRatingDto extends BaseProductDto {
    private long id;
    private double averageRating;
    private long totalRatings;
    private ProductImage productImage;

    public CustomProductRatingDto(long id, ProductImage productImage, String name, String slug, double averageRating, long totalRatings){
        this.id = id;
        this.name= name;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.productImage = productImage;
        this.slug = slug;
    }
}
