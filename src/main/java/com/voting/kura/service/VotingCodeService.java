package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.User;
import com.voting.kura.model.VoterStatus;
import com.voting.kura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VotingCodeService {
    private final UserRepository userRepository;

    @Autowired
    public VotingCodeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User generateCode(String admissionNumber) {
        User user = userRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new VotingException("User not found with admission number: " + admissionNumber));
        
        // Check if user already has an unused code that hasn't expired
        if (user.getVotingCode() != null && 
            !user.isVotingCodeUsed() && 
            user.getVotingCodeExpiresAt().isAfter(LocalDateTime.now())) {
            throw new VotingException("User already has an active voting code");
        }

        // Generate new code
        String code = generateUniqueHexCode();
        
        // Update user with new code and voter status
        user.setVotingCode(code);
        user.setVotingCodeExpiresAt(LocalDateTime.now().plusHours(24));
        user.setVotingCodeUsed(false);
        user.setVoter(true);
        user.setVoterStatus(VoterStatus.ACTIVE);

        return userRepository.save(user);
    }

    private String generateUniqueHexCode() {
        Random random = new Random();
        String code;
        do {
            // Generate a 6-character hex code
            code = String.format("%06X", random.nextInt(0xFFFFFF + 1));
        } while (userRepository.existsByVotingCode(code));
        
        return code;
    }

    public boolean validateCode(String admissionNumber, String code) {
        return userRepository.findByAdmissionNumber(admissionNumber)
                .filter(user -> user.getVotingCode() != null)
                .filter(user -> user.getVotingCode().equals(code))
                .filter(user -> !user.isVotingCodeUsed())
                .filter(user -> user.getVotingCodeExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public void markCodeAsUsed(String admissionNumber, String code) {
        User user = userRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new VotingException("User not found"));

        if (!user.getVotingCode().equals(code)) {
            throw new VotingException("Invalid voting code");
        }

        user.setVotingCodeUsed(true);
        // Keep voter status as is since they're now a verified voter
        userRepository.save(user);
    }
}