package com.laptopstore.ecommerce.dto.review;

import com.laptopstore.ecommerce.validation.review.UpdateReviewConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UpdateReviewConstraint
public class UpdateReviewDto extends BaseReviewDto {
    private Long reviewId;
    private Long productId;

    public UpdateReviewDto(Long reviewId, Long productId, String productName, int rating, String message){
        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.message = message;
    }
}
