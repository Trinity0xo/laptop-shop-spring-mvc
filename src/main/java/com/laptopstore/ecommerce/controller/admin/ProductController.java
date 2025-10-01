package com.laptopstore.ecommerce.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.util.ResponseUtils;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String showProductsPage(
            ProductFilterDto productFilterDto,
            Model model
    ) {
        PageResponse<CustomProductListDto<CustomProductDto>> response = this.productService.getAdminProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

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
        ResponseEntity<AjaxResponse<Object>> ajaxErrorResponse = ResponseUtils.getAjaxErrorResponseResponseEntity(bindingResult);
        if (ajaxErrorResponse != null) return ajaxErrorResponse;

        this.productService.createProduct(createProductDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Tạo sản phẩm thành công", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(ajaxResponse);
    }

    @GetMapping("/update/{productId}")
    public String showUpdateProductPage(
            @PathVariable long productId,
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
        ResponseEntity<AjaxResponse<Object>> ajaxErrorResponse = ResponseUtils.getAjaxErrorResponseResponseEntity(bindingResult);
        if (ajaxErrorResponse != null) return ajaxErrorResponse;

        this.productService.updateProduct(updateProductDto);

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>("Cập nhật thành công", null);

        return ResponseEntity.status(HttpStatus.OK).body(ajaxResponse);
    }

    @GetMapping("/delete/{productId}")
    public String showDeleteProductPage(
            @PathVariable long productId,
            Model model
    ) {
        Product product = this.productService.getProductById(productId);
        model.addAttribute("product", product);

        return "/admin/product/delete";
    }

    @PostMapping("/delete")
    public String deleteProduct(
            long productId,
            RedirectAttributes redirectAttributes
    ) {
        this.productService.deleteProduct(productId);

        redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công");

        return "redirect:/dashboard/product";
    }

    @GetMapping("/details/{productId}")
    public String showDetailsProductPage(
            @PathVariable long productId,
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
}
