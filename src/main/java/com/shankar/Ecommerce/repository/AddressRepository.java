package com.shankar.Ecommerce.repository;

import com.shankar.Ecommerce.entites.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
