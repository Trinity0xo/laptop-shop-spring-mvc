package com.laptopstore.ecommerce.controller.client;

import com.laptopstore.ecommerce.dto.HomeContentDto;
import com.laptopstore.ecommerce.dto.product.CustomProductDto;
import com.laptopstore.ecommerce.dto.product.CustomProductListDto;
import com.laptopstore.ecommerce.dto.product.ProductFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.exception.NotImplementException;
import com.laptopstore.ecommerce.service.ContentService;
import com.laptopstore.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ContentService contentService;
    private final ProductService productService;

    public HomeController(ContentService contentService, ProductService productService) {
        this.contentService = contentService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String showHomePage(
            Model model
    )  {
        HomeContentDto homeContent = this.contentService.getHomePageContent();
        model.addAttribute("homeContent", homeContent);

        return "/client/home";
    }

    @GetMapping("/hot-deals")
    public String showHotDealsPage(
            ProductFilterDto productFilterDto,
            Model model
    )  {
        PageResponse<CustomProductListDto<CustomProductDto>> response = this.productService.getTopDiscountProducts(productFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("productFilterDto", productFilterDto);

        return "/client/top_discount";
    }

    @GetMapping("/contact")
    public String showContactPage(){
        throw new NotImplementException();
    }

    @GetMapping("/about")
    public String showAboutPage(){
        throw new NotImplementException();
    }
}
