package com.laptopstore.ecommerce.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteReviewDto extends BaseReviewDto{
    private Long reviewId;

    public DeleteReviewDto(long reviewId, long productId, String productName){
        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
    }
}
