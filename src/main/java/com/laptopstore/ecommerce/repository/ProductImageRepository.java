package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByImageName(String imageName);
    Optional<ProductImage> findByProductIdAndIsMain(long productId, boolean isMain);
    List<ProductImage> findByProductId(long productId);
    List<ProductImage> findByProductIdIn(List<Long> productIds);
    Optional<ProductImage> findFirstByProductId(long productId);
}
