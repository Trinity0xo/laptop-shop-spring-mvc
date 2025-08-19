package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import com.laptopstore.ecommerce.dto.review.*;
import com.laptopstore.ecommerce.service.ReviewService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ClientReviewController {
    private final ReviewService reviewService;

    public ClientReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}/create")
    public String showCreateProductReviewModal(
            @PathVariable long productId,
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        CreateReviewDto createReviewDto = this.reviewService.getInformationForCreateReview(email, productId);
        model.addAttribute("createReviewDto", createReviewDto);

        return "/client/review/create_review_modal";
    }

    @PostMapping("/{productId}/create")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>> createNewReview(
            @PathVariable Long productId,
            @Valid CreateReviewDto createReviewDto,
            BindingResult bindingResult
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            AjaxResponse<Object> ajaxResponse = new AjaxResponse<>(null, errors);

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ajaxResponse);
        }

        this.reviewService.UserCreateReview(productId, email, createReviewDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Tạo đánh giá thành công", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(ajaxResponse);
    }

    @GetMapping("/{productId}/update/{reviewId}")
    public String showUpdateProductReviewModal(
            @PathVariable long productId,
            @PathVariable long reviewId,
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        UpdateReviewDto updateReviewDto = this.reviewService.getInformationForUpdateReview(email, productId, reviewId);
        model.addAttribute("updateReviewDto", updateReviewDto);

        return "/client/review/update_review_modal";
    }

    @PostMapping("/{productId}/update")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>>  updateReview(
            @PathVariable long productId,
            @Valid UpdateReviewDto updateReviewDto,
            BindingResult bindingResult
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            AjaxResponse<Object> ajaxResponse = new AjaxResponse<>(null, errors);

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ajaxResponse);
        }

        this.reviewService.UserUpdateReview(productId, email, updateReviewDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Cập nhật đánh giá thành công", null);

        return ResponseEntity.status(HttpStatus.OK).body(ajaxResponse);
    }

    @GetMapping("/{productId}/delete/{reviewId}")
    public String showDeleteProductReviewModal(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            Model model
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        DeleteReviewDto deleteReviewDto = this.reviewService.getInformationForDeleteReview(email, productId, reviewId);
        model.addAttribute("deleteReviewDto", deleteReviewDto);

        return "/client/review/delete_review_modal";
    }

    @PostMapping("{productId}/delete")
    public ResponseEntity<AjaxResponse<Object>> deleteReview(
            @PathVariable long productId,
            @RequestParam long reviewId
    )  {
        String email = AuthenticationUtils.getAuthenticatedName();

        this.reviewService.UserDeleteReview(reviewId, productId, email);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Xóa đánh giá thành công", null);


        return ResponseEntity.status(HttpStatus.OK).body(ajaxResponse);
    }
}
