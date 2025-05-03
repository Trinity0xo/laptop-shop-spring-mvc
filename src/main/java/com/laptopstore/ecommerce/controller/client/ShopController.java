package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.product.ProductCriteriaDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.service.BrandService;
import com.laptopstore.ecommerce.service.CategoryService;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ShopController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping("")
    public String showShopPage(
            ProductCriteriaDto productCriteriaDto,
            Model model
    ) throws Exception {
        Page<Product> products = this.productService.handleGetAllProducts(productCriteriaDto);
        List<Category> categories = this.categoryService.handleGetAllCategories();
        List<Brand> brands = this.brandService.handleGetAllBrands();
        model.addAttribute("productList", products.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getPageable().getPageNumber() + 1);
        model.addAttribute("resultCount", products.getTotalElements());
        model.addAttribute("query", productCriteriaDto);

        return "/client/shop";
    }

    @GetMapping("/product/{id}")
    public String showProductDetailsPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Product product = this.productService.handleGetProductById(id);
        if(product == null) {
            throw new NotFoundException("Product not found");
        }

        model.addAttribute("product", product);

        return "/client/product_details";
    }
}
