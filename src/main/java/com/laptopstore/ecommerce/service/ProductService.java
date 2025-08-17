package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.Product;

public interface ProductService {
    Product getProductById(long productId);
    Product getProductByName(String name);
    Product getProductBySlug(String slug);
    PageResponse<CustomProductListDto<CustomProductDto>> getShopProducts(ProductFilterDto productFilterDto);
    PageResponse<CustomProductListDto<CustomProductDto>> getAdminProducts(ProductFilterDto productFilterDto);
    CustomProductDetailsDto getShopProductDetailsBySlug(String productSlug);
    CustomProductDetailsDto getShopProductDetailsBySlug(String productSlug, String email);
    CustomProductDetailsDto getAdminProductDetailsById(long productId);
    CreateProductDto getInformationForCreateProduct();
    void createProduct(CreateProductDto createProductDto);
    UpdateProductDto getInformationForUpdateProduct(long productId);
    void updateProduct(UpdateProductDto updateProductDto);
    void deleteProduct(long productId);
    PageResponse<CustomProductListDto<CustomProductSoldDto>> getTopSellingProducts(ProductFilterDto productFilterDto);
    PageResponse<CustomProductListDto<CustomProductRatingDto>> getTopRatedProducts(ProductFilterDto productFilterDto);
    PageResponse<CustomProductListDto<CustomProductStockDto>> getLowStockProducts(ProductFilterDto productFilterDto);
    PageResponse<CustomProductListDto<CustomProductDiscountDto>> getDiscountProducts(ProductFilterDto productFilterDto);
    PageResponse<CustomProductListDto<CustomProductDto>> getTopDiscountProducts(ProductFilterDto productFilterDto);
}
