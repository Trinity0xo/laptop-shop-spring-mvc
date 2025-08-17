package com.laptopstore.ecommerce.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class BaseReviewDto {
    protected long productId;
    protected String productName;
    protected Integer rating;
    protected String message;
    protected Instant createdAt;
    protected Instant updatedAt;
}
