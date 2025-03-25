package com.voting.kura.service;

import com.voting.kura.dto.request.LoginRequest;
import com.voting.kura.dto.response.LoginResponse;
import com.voting.kura.exception.VotingException;
import com.voting.kura.model.User;
import com.voting.kura.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        logger.debug("Login attempt for admission number: {}", loginRequest.getAdmissionNumber());
        
        User user = userRepository.findByAdmissionNumber(loginRequest.getAdmissionNumber())
                .orElseThrow(() -> {
                    logger.debug("User not found with admission number: {}", loginRequest.getAdmissionNumber());
                    return new VotingException("Invalid admission number or password");
                });

        logger.debug("User found: {}", user);
        logger.debug("Comparing passwords - Request: {}, Stored: {}", loginRequest.getPassword(), user.getPassword());

        if (!loginRequest.getPassword().equals(user.getPassword())) {
            logger.debug("Password mismatch for user: {}", user.getAdmissionNumber());
            throw new VotingException("Invalid admission number or password");
        }

        logger.debug("Login successful for user: {}", user.getAdmissionNumber());
        return new LoginResponse(user.getId(), user.getFullName(), "Login successful");
    }
}