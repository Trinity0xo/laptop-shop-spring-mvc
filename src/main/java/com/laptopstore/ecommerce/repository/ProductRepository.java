package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.repository.custom.CustomProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.laptopstore.ecommerce.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, CustomProductRepository {
    Optional<Product> findByName(String name);
    Optional<Product> findBySlug(String slug);
}
