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
public class CustomProductDto extends BaseProductDto {
    private long id;
    private double averageRating;
    private double discountPrice;
    private ProductImage productImage;

    public CustomProductDto(Long id, ProductImage productImage, String name, String slug, long quantity, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.name =  name;
        this.slug =  slug;
        this.productImage = productImage;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CustomProductDto(long id, ProductImage productImage, String name, String slug, double averageRating, double price, float discount, double discountPrice, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.averageRating = averageRating;
        this.discount = discount;
        this.price = price;
        this.discountPrice = discountPrice;
        this.productImage = productImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static final String DEFAULT_SORT_FIELD = "createdAt";

    public static final List<String> VALID_SORT_FIELDS = List.of("id", "name", "averageRating", "discount", "price", "discountPrice", "updatedAt", "createdAt");
}
