package com.insurancecompany.insurance_server.repository;

import com.insurancecompany.insurance_server.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByUserId(Long userId);
}