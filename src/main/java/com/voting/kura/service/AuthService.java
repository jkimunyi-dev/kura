package com.voting.kura.service;

import com.voting.kura.dto.request.LoginRequest;
import com.voting.kura.dto.response.LoginResponse;
import com.voting.kura.exception.VotingException;
import com.voting.kura.model.User;
import com.voting.kura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByAdmissionNumber(loginRequest.getAdmissionNumber())
                .orElseThrow(() -> new VotingException("Invalid admission number or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new VotingException("Invalid admission number or password");
        }

        return new LoginResponse(user.getId(), user.getFullName(), "Login successful");
    }
}