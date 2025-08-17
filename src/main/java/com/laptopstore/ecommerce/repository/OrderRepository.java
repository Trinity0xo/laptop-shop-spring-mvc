package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.dto.order.MonthlyDeliveredOrderCountDto;
import com.laptopstore.ecommerce.dto.order.MonthlySalesDto;
import com.laptopstore.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUser(Long orderId, User user);

    @Query("SELECT new com.laptopstore.ecommerce.dto.order.MonthlyDeliveredOrderCountDto(MONTH(o.createdAt), COUNT(o)) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' AND YEAR(o.createdAt) = :year " +
            "GROUP BY MONTH(o.createdAt) " +
            "ORDER BY MONTH(o.createdAt)")
    List<MonthlyDeliveredOrderCountDto> findMonthlyDeliveredOrderCountsByYear(@Param("year") int year);

    @Query("SELECT new com.laptopstore.ecommerce.dto.order.MonthlySalesDto(MONTH(o.createdAt), SUM(totalPrice)) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' AND YEAR(o.createdAt) = :year " +
            "GROUP BY MONTH(o.createdAt) " +
            "ORDER BY MONTH(o.createdAt)")
    List<MonthlySalesDto> findMonthlySalesByYear (@Param("year") int year);
}
