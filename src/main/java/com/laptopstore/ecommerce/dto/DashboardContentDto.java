package com.laptopstore.ecommerce.dto;

import com.laptopstore.ecommerce.dto.category.CategorySoldCountDto;
import com.laptopstore.ecommerce.dto.order.MonthlyDeliveredOrderCountDto;
import com.laptopstore.ecommerce.dto.order.MonthlySalesDto;
import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardContentDto {
    List<Order> recentOrders;
    List<CustomProductSoldDto> topSellingProducts;
    List<CustomProductRatingDto> topRatedProducts;
    List<CustomProductStockDto> lowStockProducts;
    List<CustomReviewDto> recentReviews;
    List<MonthlyDeliveredOrderCountDto> monthlyDeliveredOrderCounts;
    List<MonthlySoldProductCountDto> monthlySoldProductCounts;
    List<MonthlySalesDto> monthlySales;
    List<CategorySoldCountDto> categorySoldCounts;
    long orderCount;
    long productCount;
    long userCount;
    long categoryCount;
}
