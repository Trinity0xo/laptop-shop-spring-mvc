package com.laptopstore.ecommerce.repository.custom;

import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.dto.review.ReviewFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomReviewRepository {
    Page<CustomReviewDto> findAllReviews(Pageable pageable);
    Page<CustomReviewDto> findAllReviews(ReviewFilterDto reviewFilterDto, Pageable pageable);
    Page<CustomReviewDto> findAllProductReviews(long productId, ReviewFilterDto reviewFilterDto, Pageable pageable);
}
