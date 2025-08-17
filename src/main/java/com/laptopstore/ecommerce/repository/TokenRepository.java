package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.model.Token;
import com.laptopstore.ecommerce.util.constant.TokenTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValueAndType(String value, TokenTypeEnum type);
}
