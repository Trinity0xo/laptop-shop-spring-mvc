package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public void handleCreateProductImage(String imageName, Product product){
        ProductImage productImage = new ProductImage();
        productImage.setImageName(imageName);
        productImage.setProduct(product);

        this.productImageRepository.save(productImage);
    }

    public ProductImage handleGetProductImageByName(String imageName){
        return this.productImageRepository.findByImageName(imageName).orElse(null);
    }

    public void handleDeleteProductImageById(Long id){
        this.productImageRepository.deleteById(id);
    }

    public List<ProductImage> handleGetProductImagesByProductId(long id){
        return this.productImageRepository.findByProductId(id);
    }
}
