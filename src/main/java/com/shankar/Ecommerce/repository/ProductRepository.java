package com.shankar.Ecommerce.repository;

import com.shankar.Ecommerce.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductId(Long categoryId);
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
}
