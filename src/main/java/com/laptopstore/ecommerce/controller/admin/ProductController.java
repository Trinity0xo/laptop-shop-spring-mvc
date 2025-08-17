package com.laptopstore.ecommerce.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.review.ReviewFilterDto;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/product")
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public String showProductsPage(
            ProductFilterDto productFilterDto,
            Model model
    ) {
        PageResponse<CustomProductListDto<CustomProductDto>> response = this.productService.getAdminProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productCriteriaDto", productFilterDto);

        return "/admin/product/index";
    }

    @GetMapping("/create")
    public String showCreateProductPage(
            Model model
    ) {
          CreateProductDto createProductDto = this.productService.getInformationForCreateProduct();
          model.addAttribute("createProductDto", createProductDto);

        return "/admin/product/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>> createProduct(
            @Valid CreateProductDto createProductDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("validation error", errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponse);
        }

        this.productService.createProduct(createProductDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Create new product success", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(ajaxResponse);
    }

    @GetMapping("/update/{productId}")
    public String showUpdateProductPage(
            @PathVariable Long productId,
            Model model
    ) {
        UpdateProductDto updateProductDto = this.productService.getInformationForUpdateProduct(productId);
        model.addAttribute("updateProductDto", updateProductDto);

        return "/admin/product/update";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>> updateProduct(
            @Valid UpdateProductDto updateProductDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("validation error", errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponse);
        }

        this.productService.updateProduct(updateProductDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Update product success", null);

        return ResponseEntity.status(HttpStatus.OK).body(ajaxResponse);
    }

    @GetMapping("/delete/{productId}")
    public String showDeleteProductPage(
            @PathVariable Long productId,
            Model model
    ) {
        model.addAttribute("productId", productId);

        return "/admin/product/delete";
    }

    @PostMapping("/delete")
    public String deleteProduct(
            Long productId,
            RedirectAttributes redirectAttributes
    ) {
        this.productService.deleteProduct(productId);

        redirectAttributes.addFlashAttribute("successMessage", "Delete product success");

        return "redirect:/dashboard/product";
    }

    @GetMapping("/details/{productId}")
    public String showDetailsProductPage(
            @PathVariable Long productId,
            Model model
    ) {
        CustomProductDetailsDto customProductDetailsDto = this.productService.getAdminProductDetailsById(productId);
        model.addAttribute("customProductDetailsDto", customProductDetailsDto);

        return "/admin/product/details";
    }

    @GetMapping("/low-stock")
    public String showLowStockPage(
            ProductFilterDto productFilterDto,
            Model model
    ) {
        PageResponse<CustomProductListDto<CustomProductStockDto>> response = this.productService.getLowStockProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

        return "/admin/product/low_stock";
    }

    @GetMapping("/discount")
    public String showDiscountPage(
            ProductFilterDto productFilterDto,
            Model model
    ) {
        PageResponse<CustomProductListDto<CustomProductDiscountDto>> response = this.productService.getDiscountProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

        return "/admin/product/discount";
    }

    @GetMapping("/details/{productId}/review")
    public String showProductReviewsPage(
            @PathVariable Long productId,
            ReviewFilterDto reviewFilterDto,
            Model model
    )  {
        PageResponse<CustomProductDetailsDto> response = this.reviewService.getProductReviews(productId, reviewFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("reviewFilterDto", reviewFilterDto);


        return "/admin/product/product_reviews";
    }
}
