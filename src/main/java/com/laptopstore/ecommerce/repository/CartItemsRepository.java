package com.laptopstore.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.CartItem;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
    int countByCart(Cart cart);

    @Query("SELECT COALESCE(SUM((ci.product.price - (ci.product.price * ci.product.discount / 100)) * ci.quantity), 0) " +
            "FROM CartItem ci " +
            "WHERE ci.cart = :cart AND ci.quantity <= ci.product.quantity")
    Double calculateCartTotalPrice(@Param("cart") Cart cart);
}
