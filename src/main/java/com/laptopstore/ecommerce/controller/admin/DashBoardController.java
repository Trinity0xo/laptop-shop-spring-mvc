package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.DashboardContentDto;
import com.laptopstore.ecommerce.dto.product.CustomProductListDto;
import com.laptopstore.ecommerce.dto.product.CustomProductRatingDto;
import com.laptopstore.ecommerce.dto.product.CustomProductSoldDto;
import com.laptopstore.ecommerce.dto.product.ProductFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
    private final ContentService contentService;
    private final ProductService productService;

    public DashBoardController(ContentService contentService, ProductService productService) {
        this.contentService = contentService;
        this.productService = productService;
    }

    @GetMapping("")
    public String showDashboard(
            Model model
    )  {
        DashboardContentDto dashboardContent = this.contentService.getDashboardContent();
        model.addAttribute("dashboardContent", dashboardContent);

        return "admin/dashboard";
    }

    @GetMapping("/top-selling")
    public String showTopSellingProductsPage(
            ProductFilterDto productFilterDto,
            Model model
    )  {
        PageResponse<CustomProductListDto<CustomProductSoldDto>> response = this.productService.getTopSellingProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

        return "/admin/top-selling";
    }

    @GetMapping("/top-rated")
    public String showTopRatedProductsPage(
            ProductFilterDto productFilterDto,
            Model model
    )  {
        PageResponse<CustomProductListDto<CustomProductRatingDto>> response = this.productService.getTopRatedProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

        return "/admin/top-rated";
    }
}
