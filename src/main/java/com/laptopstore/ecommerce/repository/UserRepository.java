package com.laptopstore.ecommerce.repository;

import com.laptopstore.ecommerce.model.Role;
import com.laptopstore.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
    long countUserByRole(Role role);
}
