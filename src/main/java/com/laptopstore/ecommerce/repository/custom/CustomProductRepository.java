package com.laptopstore.ecommerce.repository.custom;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CustomProductRepository {
    Page<CustomProductDto> findShopProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductDto> findAllProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductSoldDto> findTopSellingProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductSoldDto> findTopSellingProducts(Instant time, Pageable pageable);
    Page<CustomProductRatingDto> findTopRatedProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductRatingDto> findTopRatedProducts(Instant time, Pageable pageable);
    Page<CustomProductDto> findTopDiscountProducts(Pageable pageable);
    Page<CustomProductDto> findTopDiscountProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductStockDto> findLowStockProducts(Pageable pageable);
    Page<CustomProductStockDto> findLowStockProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductDiscountDto> findDiscountProducts(ProductFilterDto productFilterDto, Pageable pageable);
    Page<CustomProductDto> findRelatedProducts(Product product, Pageable pageable);
}
