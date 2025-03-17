package com.insurancecompany.insurance_server.controller;

import com.insurancecompany.insurance_server.model.Claim;
import com.insurancecompany.insurance_server.model.Policy;
import com.insurancecompany.insurance_server.service.ClaimService;
import com.insurancecompany.insurance_server.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @Autowired
    private PolicyService policyService;

    @GetMapping("/policy/{policyId}")
    public List<Claim> getClaimsByPolicy(@PathVariable Long policyId) {
        return claimService.getClaimsByPolicy(policyId);
    }

    @GetMapping
    public ResponseEntity<?> getAllClaims() {
        try {
            List<Claim> claims = claimService.getAllClaims();
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch claims");
        }
    }

    // Эндпоинт для получения заявок пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getClaimsByUser(@PathVariable Long userId) {
        try {
            List<Claim> claims = claimService.getClaimsByUser(userId);
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch claims");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClaim(@RequestBody Map<String, Object> request) {
        try {
            // Проверяем, что policyId передан
            if (!request.containsKey("policyId") || request.get("policyId") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Policy ID is missing");
            }

            // Извлекаем policyId и преобразуем в Long
            Long policyId = ((Number) request.get("policyId")).longValue();

            // Проверяем, что status передан
            if (!request.containsKey("status") || request.get("status") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status is missing");
            }

            // Извлекаем status
            String status = (String) request.get("status");

            // Устанавливаем связь с полисом
            Policy policy = policyService.getPolicyById(policyId);
            if (policy == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Policy not found");
            }

            // Создаем новую заявку
            Claim claim = new Claim();
            claim.setPolicy(policy); // Устанавливаем связь с полисом
            claim.setStatus(status); // Устанавливаем статус
            claim.setClaimNumber(generateClaimNumber()); // Генерируем уникальный claimNumber

            // Сохраняем заявку
            Claim createdClaim = claimService.createClaim(claim);

            // Возвращаем созданную заявку
            return ResponseEntity.ok(createdClaim);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create claim");
        }
    }

    // Метод для генерации уникального claimNumber
    private String generateClaimNumber() {
        return "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }



    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveClaim(@PathVariable Long id) {
        try {
            Claim claim = claimService.getClaimById(id);
            if (claim == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Claim not found");
            }

            claim.setStatus("APPROVED");
            claimService.saveClaim(claim);
            return ResponseEntity.ok("Claim approved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve claim");
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectClaim(@PathVariable Long id) {
        try {
            Claim claim = claimService.getClaimById(id);
            if (claim == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Claim not found");
            }

            claim.setStatus("REJECTED");
            claimService.saveClaim(claim);
            return ResponseEntity.ok("Claim rejected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reject claim");
        }
    }


}