package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.dto.product.MonthlySoldProductCountDto;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.OrderItem;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByOrderUserAndOrderStatusAndProduct(User user, OrderStatusEnum orderStatus, Product product);

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderItem od JOIN od.order o WHERE o.status = 'DELIVERED' AND od.product.id = :productId")
    Long countSoldProductByProductId(@Param("productId") long productId);

    @Query("SELECT new com.laptopstore.ecommerce.dto.product.MonthlySoldProductCountDto(MONTH(o.createdAt), COALESCE(SUM(od.quantity), 0)) " +
            "FROM OrderItem od " +
            "JOIN od.order o " +
            "WHERE o.status = 'DELIVERED' AND YEAR(o.createdAt) = :year " +
            "GROUP BY MONTH(o.createdAt) " +
            "ORDER BY MONTH(o.createdAt)")
    List<MonthlySoldProductCountDto> findMonthlySoldProductCountsByYear(@Param("year") int year);
}
