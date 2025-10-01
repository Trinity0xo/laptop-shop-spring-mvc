package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.product.CustomProductDetailsDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.dto.review.ReviewFilterDto;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public String showReviewsPage(
            ReviewFilterDto reviewFilterDto,
            Model model
    ){
        PageResponse<List<CustomReviewDto>> response = this.reviewService.getReviews(reviewFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("reviewFilterDto", reviewFilterDto);

        return "/admin/review/index";
    }

    @GetMapping("/details/{reviewId}")
    public String showReviewDetailsPage(@PathVariable long reviewId, Model model
    ){
        Review review = this.reviewService.getReviewDetails(reviewId);
        model.addAttribute("review", review);

        return "/admin/review/details";
    }

    @GetMapping("/{productId}")
    public String showProductReviewsPage(
            @PathVariable long productId,
            ReviewFilterDto reviewFilterDto,
            Model model
    )  {
        PageResponse<CustomProductDetailsDto> response = this.reviewService.getProductReviews(productId, reviewFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("reviewFilterDto", reviewFilterDto);


        return "/admin/review/product_reviews";
    }
}
