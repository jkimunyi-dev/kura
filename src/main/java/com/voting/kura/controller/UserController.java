package com.voting.kura.controller;

import com.voting.kura.model.User;
import com.voting.kura.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllUsers() {
        logger.info("Received request to get all users");
        List<User> users = userService.getAllUsers();
        logger.info("Found {} users", users.size());
        
        List<Map<String, String>> response = users.stream()
            .map(user -> Map.of(
                "admissionNumber", user.getAdmissionNumber(),
                "fullName", user.getFullName(),
                "facultyCode", user.getFacultyCode()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testConnection() {
        logger.info("Testing database connection");
        long userCount = userService.getUserCount(); // Add this method to UserService
        return ResponseEntity.ok("Database connection successful. User count: " + userCount);
    }
}