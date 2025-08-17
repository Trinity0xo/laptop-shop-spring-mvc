package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.product.CustomProductDetailsDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.review.*;
import com.laptopstore.ecommerce.model.Review;

import java.util.List;

public interface ReviewService {
    Review getUserReviewOnProduct(long reviewId, long productId, String email);
    PageResponse<List<CustomReviewDto>> getReviews(ReviewFilterDto reviewFilterDto);
    Review getReviewDetails(long reviewId);
    PageResponse<CustomProductDetailsDto> getProductReviews(long productId, ReviewFilterDto reviewFilterDto);
    PageResponse<CustomProductDetailsDto> getProductReviews(String productSlug, ReviewFilterDto reviewFilterDto);
    PageResponse<CustomProductDetailsDto> getProductReviews(String productSlug, String email, ReviewFilterDto reviewFilterDto);
    CreateReviewDto getInformationForCreateReview(String email, long productId);
    void UserCreateReview(long productId, String email, CreateReviewDto createReviewDto);
    UpdateReviewDto getInformationForUpdateReview(String email, long productId, long reviewId);
    void UserUpdateReview(long productId, String email, UpdateReviewDto updateReviewDto);
    DeleteReviewDto getInformationForDeleteReview(String email, long productId, long reviewId);
    void UserDeleteReview(long reviewId, long productId, String email);
}
