package com.example.simpleshop.repo;

import com.example.simpleshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // No extra methods needed for basic CRUD
}
