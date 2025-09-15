package com.laptopstore.ecommerce.dto.review;

import com.laptopstore.ecommerce.validation.review.CreateReviewConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@CreateReviewConstraint
public class CreateReviewDto extends BaseReviewDto {
    private Long productId;

    public CreateReviewDto(Long productId, String productName){
        this.productId = productId;
        this.productName = productName;
    }
}
