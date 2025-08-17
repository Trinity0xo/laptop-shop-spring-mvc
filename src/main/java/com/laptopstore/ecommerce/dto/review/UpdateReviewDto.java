package com.laptopstore.ecommerce.dto.review;

import com.laptopstore.ecommerce.util.validation.review.UpdateReviewConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UpdateReviewConstraint
public class UpdateReviewDto extends BaseReviewDto {
    private Long reviewId;

    public UpdateReviewDto(long reviewId, long productId, String productName, int rating, String message){
        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.message = message;
    }
}
