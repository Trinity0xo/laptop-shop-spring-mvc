package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import com.laptopstore.ecommerce.dto.product.ProductCriteriaDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final RoleService roleService;

    public DashBoardController(ProductService productService, UserService userService, CategoryService categoryService, OrderService orderService, RoleService roleService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showDashboard(
            ProductCriteriaDto productCriteriaDto,
            Model model
    ) throws Exception {
        PageableCriteriaDto pageableCriteriaDto = new PageableCriteriaDto();

        Page<Product> products = this.productService.handleGetTopSaleProducts(productCriteriaDto, pageableCriteriaDto);
        Page<Order> orders = this.orderService.handleGetRecentOrders(pageableCriteriaDto);
        Role userRole = this.roleService.handleGetRoleByName("USER");

        model.addAttribute("productList", products.getContent());
        model.addAttribute("orderList", orders.getContent());
        model.addAttribute("orderCount", this.orderService.handleCountOrder());
        model.addAttribute("categoryCount", this.categoryService.handleCountCategory());
        model.addAttribute("userCount", this.userService.handleCountUserByRole(userRole));
        model.addAttribute("productCount", this.productService.handleCountProduct());
        model.addAttribute("query", productCriteriaDto);

        return "admin/dashboard";
    }
}
