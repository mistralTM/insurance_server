package com.insurancecompany.insurance_server.service;

import com.insurancecompany.insurance_server.model.Policy;
import com.insurancecompany.insurance_server.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyService {
    @Autowired
    private PolicyRepository policyRepository;

    public List<Policy> getPoliciesByUser(Long userId) {
        return policyRepository.findByUserId(userId);
    }

    // Метод для создания полиса
    public Policy createPolicy(Policy policy) {
        // Генерация уникального номера полиса
        String policyNumber = generatePolicyNumber();
        policy.setPolicyNumber(policyNumber);

        // Сохранение полиса
        return policyRepository.save(policy);
    }

    // Метод для генерации номера полиса
    private String generatePolicyNumber() {
        return "POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id).orElse(null);
    }

    public Policy savePolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }
}