package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.custom.CustomReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review>, CustomReviewRepository {
    Optional<Review> findByUserAndProduct(User user, Product product);
    Optional<Review> findByIdAndUserAndProduct(long id, User user, Product product);
    boolean existsByUserAndProduct(User user, Product product);
    long countByProductAndRating(Product product, int rating);
    long countByProduct(Product product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product")
    Double findAverageRatingByProduct(@Param("product") Product product);

    @Query("SELECT COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Review WHERE product = :product) AS percentage FROM Review WHERE product = :product AND rating = :rating")
    Double findAverageRatingByProductAndRating(@Param("product") Product product, @Param("rating") int rating);
}
