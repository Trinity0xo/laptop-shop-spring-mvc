package com.laptopstore.ecommerce.controller.client;

import java.util.List;

import com.laptopstore.ecommerce.dto.product.ProductCriteriaDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.BrandService;
import com.laptopstore.ecommerce.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.service.ProductService;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage(
    ) {
        return "/client/home";
    }
}
