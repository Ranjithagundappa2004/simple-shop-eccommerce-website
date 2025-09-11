package com.example.simpleshop.repo;

import com.example.simpleshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // Find user by email for login
}
