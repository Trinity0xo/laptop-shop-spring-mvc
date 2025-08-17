package com.laptopstore.ecommerce.dto.review;

import com.laptopstore.ecommerce.util.validation.review.CreateReviewConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CreateReviewConstraint
public class CreateReviewDto extends BaseReviewDto {
    public CreateReviewDto(long productId, String productName){
        this.productId = productId;
        this.productName = productName;
    }
}
