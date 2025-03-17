package com.insurancecompany.insurance_server.controller;

import com.insurancecompany.insurance_server.dto.AuthRequest;
import com.insurancecompany.insurance_server.dto.AuthResponse;
import com.insurancecompany.insurance_server.model.User;
import com.insurancecompany.insurance_server.security.JwtTokenProvider;
import com.insurancecompany.insurance_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            User user = userService.findByUsername(request.getUsername());
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String role = user.getRole();
            if (role == null || role.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User role is not defined");
            }

            String token = jwtTokenProvider.createToken(user.getUsername(), role);

            // Возвращаем токен, роль и id пользователя
            return ResponseEntity.ok(new AuthResponse(token, role, user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }
    // Эндпоинт для проверки токена
    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String token) {
        try {
            // Удаляем префикс "Bearer " из токена
            String jwtToken = token.replace("Bearer ", "");

            // Проверяем, действителен ли токен
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Получаем имя пользователя из токена
            String username = jwtTokenProvider.getUsername(jwtToken);

            // Получаем пользователя из базы данных
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            // Возвращаем информацию о пользователе
            return ResponseEntity.ok(new AuthResponse(jwtToken, user.getRole(), user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token check failed");
        }
    }
}