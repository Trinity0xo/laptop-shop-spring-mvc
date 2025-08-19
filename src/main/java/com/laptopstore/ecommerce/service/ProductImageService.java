package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.ProductImage;

import java.util.List;

public interface ProductImageService {
    ProductImage getProductImageByName(String imageName);
    ProductImage getProductMainImageById(long id);
    ProductImage getAnyImageByProductId(long productId);
    void saveProductImage(ProductImage productImage);
    List<ProductImage> getProductImagesByProductId(long productId);
    void createProductImages(List<String> imageNames, Product product, boolean skipMain);
    void deleteProductImageById(Long imageId);
}
