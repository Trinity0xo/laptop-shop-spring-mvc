package com.laptopstore.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Cart;
import com.laptopstore.ecommerce.model.CartDetails;
import java.util.List;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {
    List<CartDetails> findByCart(Cart cart);
    int countByCart(Cart cart);
}
