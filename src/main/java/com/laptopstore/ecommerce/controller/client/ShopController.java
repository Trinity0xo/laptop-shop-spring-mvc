package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.review.ReviewFilterDto;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.service.ReviewService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ProductService productService;
    private final ReviewService reviewService;

    public ShopController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public String showShopPage(
            ProductFilterDto productFilterDto,
            Model model
    )  {
        PageResponse<CustomProductListDto<CustomProductDto>> response = this.productService.getShopProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productCriteriaDto", productFilterDto);

        return "/client/shop";
    }

    @GetMapping("/product/{productSlug}")
    public String showProductDetailsPage(
            @PathVariable String productSlug,
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();
        if (email != null && !email.isEmpty()) {
            model.addAttribute("productDetails", this.productService.getShopProductDetailsBySlug(productSlug, email));
        }else{
            model.addAttribute("productDetails", this.productService.getShopProductDetailsBySlug(productSlug));
        }

        return "/client/product_details";
    }

    @GetMapping("/product/{productSlug}/reviews")
    public String showAllReviewsPage(
            @PathVariable String productSlug,
            ReviewFilterDto reviewFilterDto,
            Model model
    )  {
        PageResponse<CustomProductDetailsDto> response;
        String email = AuthenticationUtils.getAuthenticatedName();
        if (email != null && !email.isEmpty()) {
            response = this.reviewService.getProductReviews(productSlug, email, reviewFilterDto);
        }
        else {
            response = this.reviewService.getProductReviews(productSlug, reviewFilterDto);
        }
        model.addAttribute("response", response);
        model.addAttribute("reviewFilterDto", reviewFilterDto);

        return "/client/review/product_reviews";
    }
}
