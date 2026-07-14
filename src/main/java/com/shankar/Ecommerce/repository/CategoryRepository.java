package com.shankar.Ecommerce.repository;

import com.shankar.Ecommerce.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
