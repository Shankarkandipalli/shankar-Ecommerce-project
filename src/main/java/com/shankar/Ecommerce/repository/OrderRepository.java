package com.shankar.Ecommerce.repository;

import com.shankar.Ecommerce.entites.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
