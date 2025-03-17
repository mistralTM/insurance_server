package com.insurancecompany.insurance_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimNumber; // Уникальный номер заявки
    private String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false) // поле не может быть null
    private Policy policy;
}