package com.insurancecompany.insurance_server.service;

import com.insurancecompany.insurance_server.model.User;
import com.insurancecompany.insurance_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Проверка на уникальность имени пользователя
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Устанавливаем роль по умолчанию
        user.setRole("USER");

        // Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохраняем пользователя в базе данных
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод для поиска пользователя по ID
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Сохранение пользователя
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}