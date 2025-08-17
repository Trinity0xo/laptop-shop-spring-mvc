package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.repository.ProductImageRepository;
import com.laptopstore.ecommerce.service.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductImage getProductMainImageById(long productId){
        return productImageRepository.findByProductIdAndIsMain(productId, true).orElse(null);
    }

    @Override
    public void createProductImages(List<String> imageNames, Product product, boolean skipMain) {
        List<ProductImage> productImages = new ArrayList<>();

        for (String imageName : imageNames){
            boolean isMain = !skipMain;

            ProductImage productImage = new ProductImage(
                    product, imageName, isMain
            );

            productImages.add(productImage);
        }

        this.productImageRepository.saveAll(productImages);
    }

    @Override
    public ProductImage getProductImageByName(String imageName){
        return this.productImageRepository.findByImageName(imageName).orElse(null);
    }

    @Override
    public void deleteProductImageById(Long id){
        this.productImageRepository.deleteById(id);
    }

    @Override
    public List<ProductImage> getProductImagesByProductId(long id){
        return this.productImageRepository.findByProductId(id);
    }
}
