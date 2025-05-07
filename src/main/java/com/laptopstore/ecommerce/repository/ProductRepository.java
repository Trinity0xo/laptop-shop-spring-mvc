package com.laptopstore.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Product;

import java.time.Instant;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query(
            "SELECT p FROM Product p " +
            "JOIN p.productImages pi " +
            "JOIN p.orderDetails od " +
            "JOIN od.order o " +
            "WHERE o.orderStatus = 'DELIVERED' AND o.createdAt >= :time " +
            "GROUP BY p.id " +
            "ORDER BY SUM(od.quantity) DESC"
    )
    Page<Product> findTopSaleProducts(@Param("time") Instant time, Pageable pageable);
}
