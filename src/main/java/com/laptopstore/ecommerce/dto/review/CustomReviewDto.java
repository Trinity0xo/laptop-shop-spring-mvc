package com.laptopstore.ecommerce.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CustomReviewDto extends BaseReviewDto{
    private long id;
    private String userAvatar;
    private String userEmail;

    public CustomReviewDto(long id, long productId, String productName, String userAvatar, String userEmail, Integer rating, Instant createdAt, Instant updatedAt)
    {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.userAvatar = userAvatar;
        this.userEmail = userEmail;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CustomReviewDto(long id, String userAvatar, String userEmail, Integer rating, Instant createdAt, Instant updatedAt)
    {
        this.id = id;
        this.userAvatar = userAvatar;
        this.userEmail = userEmail;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public static final String DEFAULT_SORT_FIELD = "createdAt";

    public static final List<String> VALID_SORT_FIELDS = List.of("id", "productName", "userEmail", "rating", "createdAt", "updatedAt");
}
