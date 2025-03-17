package com.insurancecompany.insurance_server.dto;

public class AuthResponse {
    private String token;
    private String role;
    private Long userId; // Добавляем поле для id пользователя

    public AuthResponse(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    // Геттеры и сеттеры
    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }
}