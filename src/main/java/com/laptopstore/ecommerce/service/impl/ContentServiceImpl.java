package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.DashboardContentDto;
import com.laptopstore.ecommerce.dto.HomeContentDto;
import com.laptopstore.ecommerce.dto.category.CategorySoldCountDto;
import com.laptopstore.ecommerce.dto.order.MonthlyDeliveredOrderCountDto;
import com.laptopstore.ecommerce.dto.order.MonthlySalesDto;
import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.repository.*;
import com.laptopstore.ecommerce.service.ContentService;
import com.laptopstore.ecommerce.util.DateTimeUtils;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.constant.RoleEnum;
import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemsRepository orderItemsRepository;

    public ContentServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository, UserRepository userRepository, RoleRepository roleRepository, ReviewRepository reviewRepository, OrderItemsRepository orderItemsRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.reviewRepository = reviewRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public HomeContentDto getHomePageContent(){
        Pageable pageable = PaginationUtils.createPageable(8);

        List<Category> categories = this.categoryRepository.findAll();
        List<CustomProductDto> topDiscountProducts = this.productRepository.findTopDiscountProducts(pageable).getContent();

        return new HomeContentDto(
                topDiscountProducts,
                categories
        );
    }

    @Override
    public DashboardContentDto getDashboardContent() {
        int limit = 10;
        Pageable pageable = PaginationUtils.createPageable(limit);

        long orderCount = this.orderRepository.count();
        long productCount = this.productRepository.count();
        long categoryCount = this.categoryRepository.count();

        Role userRole = this.roleRepository.findBySlug(RoleEnum.USER.name()).orElse(null);
        long userCount = 0;
        if (userRole != null) {
            userCount = userRepository.countUserByRole(userRole);
        }

        int currentYear = LocalDate.now().getYear();

        List<MonthlyDeliveredOrderCountDto> monthlyDeliveredOrderCounts = this.orderRepository.findMonthlyDeliveredOrderCountsByYear(currentYear);
        List<MonthlySoldProductCountDto> monthlySoldProductCounts = this.orderItemsRepository.findMonthlySoldProductCountsByYear(currentYear);
        List<MonthlySalesDto> monthlySalesList = this.orderRepository.findMonthlySalesByYear(currentYear);

        Map<Integer, Long> resultOrderCounts = new HashMap<>();
        Map<Integer, Long> resultProductCounts = new HashMap<>();
        Map<Integer, Double> resultSales = new HashMap<>();

        for (MonthlyDeliveredOrderCountDto dto : monthlyDeliveredOrderCounts) {
            resultOrderCounts.put(dto.getMonth(), dto.getOrderCount());
        }

        for (MonthlySoldProductCountDto dto : monthlySoldProductCounts) {
            resultProductCounts.put(dto.getMonth(), dto.getProductCount());
        }

        for (MonthlySalesDto dto : monthlySalesList) {
            resultSales.put(dto.getMonth(), dto.getSalesValue());
        }

        for (int month = 1; month <= 12; month++) {
            resultOrderCounts.putIfAbsent(month, 0L);
            resultProductCounts.putIfAbsent(month, 0L);
            resultSales.putIfAbsent(month, 0D);
        }

        List<MonthlyDeliveredOrderCountDto> filledMonthlyDeliveredOrderCounts = new ArrayList<>();
        List<MonthlySoldProductCountDto> filledMonthlySoldProductCounts = new ArrayList<>();
        List<MonthlySalesDto> filledMonthlySales = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            filledMonthlyDeliveredOrderCounts.add(new MonthlyDeliveredOrderCountDto(month, resultOrderCounts.get(month)));
            filledMonthlySoldProductCounts.add(new MonthlySoldProductCountDto(month, resultProductCounts.get(month)));
            filledMonthlySales.add(new MonthlySalesDto(month, resultSales.get(month)));
        }

        List<CategorySoldCountDto> categorySoldCounts = this.categoryRepository.findCategorySoldCountsByYear(currentYear);

        Instant time = DateTimeUtils.getInstantDaysAgo(7);

        List<CustomProductSoldDto> topSellingProducts = this.productRepository.findTopSellingProducts(time, pageable).getContent();
        List<CustomProductRatingDto> topRatedProducts = this.productRepository.findTopRatedProducts(time, pageable).getContent();

        Pageable orderPageable = PaginationUtils.createPageable(limit, "createdAt", SortDirectionEnum.DESC);
        List<Order> recentOrders = this.orderRepository.findAll(orderPageable).getContent();

        List<CustomProductStockDto> lowStockProducts = this.productRepository.findLowStockProducts(pageable).getContent();
        List<CustomReviewDto> recentReviews = this.reviewRepository.findAllReviews(pageable).getContent();

        return new DashboardContentDto(
                recentOrders,
                topSellingProducts,
                topRatedProducts,
                lowStockProducts,
                recentReviews,
                filledMonthlyDeliveredOrderCounts,
                filledMonthlySoldProductCounts,
                filledMonthlySales,
                categorySoldCounts,
                orderCount,
                productCount,
                userCount,
                categoryCount
        );
    }
}
