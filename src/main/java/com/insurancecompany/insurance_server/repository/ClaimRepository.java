package com.insurancecompany.insurance_server.repository;

import com.insurancecompany.insurance_server.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByPolicyId(Long policyId);

    // Метод для поиска заявок по userId через связь с Policy
    @Query("SELECT c FROM Claim c JOIN c.policy p WHERE p.user.id = :userId")
    List<Claim> findByUserId(@Param("userId") Long userId);

}