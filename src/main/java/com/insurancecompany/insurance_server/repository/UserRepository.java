package com.insurancecompany.insurance_server.repository;

import com.insurancecompany.insurance_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}