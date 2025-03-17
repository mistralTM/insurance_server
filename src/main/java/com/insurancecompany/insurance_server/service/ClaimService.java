package com.insurancecompany.insurance_server.service;

import com.insurancecompany.insurance_server.model.Claim;
import com.insurancecompany.insurance_server.repository.ClaimRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClaimService {
    @Autowired
    private ClaimRepository claimRepository;

    @Transactional
    public Claim createClaim(Claim claim) {
        // Сохраняем заявку
        return claimRepository.save(claim);
    }

    // Метод для генерации уникального claim_number
    private String generateClaimNumber() {
        return "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public List<Claim> getClaimsByPolicy(Long policyId) {
        return claimRepository.findByPolicyId(policyId);
    }

    // Метод для получения всех заявок
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    // Метод для получения заявок пользователя
    public List<Claim> getClaimsByUser(Long userId) {
        return claimRepository.findByUserId(userId);
    }
    public Claim getClaimById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    @Transactional
    public Claim saveClaim(Claim claim) {
        return claimRepository.save(claim);
    }
}