package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByImageName(String imageName);
    List<ProductImage> findByProductId(Long productId);
}
