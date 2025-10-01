package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.dto.category.CategorySoldCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findBySlug(String slug);

    @Query("SELECT new com.laptopstore.ecommerce.dto.category.CategorySoldCountDto(" +
            "c.name, " +
            "COALESCE(SUM(oi.quantity), 0)) " +
            "FROM Category c " +
            "LEFT JOIN c.products p " +
            "LEFT JOIN OrderItem oi ON oi.product = p " +
            "LEFT JOIN oi.order o " +
            "WHERE (o.status = 'DELIVERED' AND YEAR(o.createdAt) = :year) OR o IS NULL " +
            "GROUP BY c.name")
    List<CategorySoldCountDto> findCategorySoldCountsByYear(@Param("year") int year);
}