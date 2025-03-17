package com.insurancecompany.insurance_server.controller;

import com.insurancecompany.insurance_server.model.Policy;
import com.insurancecompany.insurance_server.model.User;
import com.insurancecompany.insurance_server.service.PolicyService;
import com.insurancecompany.insurance_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPoliciesByUser(@PathVariable Long userId) {
        try {
            List<Policy> policies = policyService.getPoliciesByUser(userId);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch policies");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPolicy(@RequestBody Policy policy) {
        try {
            // Логирование входящих данных
            System.out.println("Received policy: " + policy);

            // Устанавливаем статус "ожидает одобрения"
            policy.setStatus("PENDING");

            // Проверяем, есть ли пользователь в запросе
            if (policy.getUser() == null || policy.getUser().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is missing");
            }

            // Устанавливаем пользователя
            User user = userService.findById(policy.getUser().getId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
            policy.setUser(user);

            // Сохраняем полис
            Policy createdPolicy = policyService.createPolicy(policy);
            return ResponseEntity.ok(createdPolicy);
        } catch (Exception e) {
            e.printStackTrace(); // Логирование стека ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create policy");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePolicy(@PathVariable Long id, @RequestBody Policy updatedPolicy) {
        try {
            Policy policy = policyService.getPolicyById(id);
            if (policy == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy not found");
            }

            // Обновляем данные полиса
            policy.setPolicyType(updatedPolicy.getPolicyType());
            policy.setStartDate(updatedPolicy.getStartDate());
            policy.setEndDate(updatedPolicy.getEndDate());
            policy.setStatus(updatedPolicy.getStatus());

            // Сохраняем обновленный полис
            Policy savedPolicy = policyService.savePolicy(policy);
            return ResponseEntity.ok(savedPolicy);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update policy");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePolicy(@PathVariable Long id) {
        try {
            Policy policy = policyService.getPolicyById(id);
            if (policy == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy not found");
            }

            policyService.deletePolicy(id);
            return ResponseEntity.ok("Policy deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete policy");
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllPolicies() {
        try {
            List<Policy> policies = policyService.getAllPolicies();
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch policies");
        }
    }

}