package com.voting.kura.controller;

import com.voting.kura.dto.request.LoginRequest;
import com.voting.kura.dto.response.LoginResponse;
import com.voting.kura.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.debug("Received login request for admission number: {}", loginRequest.getAdmissionNumber());
        try {
            LoginResponse response = authService.login(loginRequest);
            logger.debug("Login successful for admission number: {}", loginRequest.getAdmissionNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for admission number: {}", loginRequest.getAdmissionNumber(), e);
            throw e;
        }
    }
}