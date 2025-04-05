package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.User;
import com.voting.kura.model.VoterStatus;
import com.voting.kura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VotingCodeService {
    private final UserRepository userRepository;

    @Autowired
    public VotingCodeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> getCodeStatus(String admissionNumber) {
        User user = userRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new VotingException("User not found with admission number: " + admissionNumber));

        Map<String, Object> status = new HashMap<>();
        status.put("hasCode", user.getVotingCode() != null);
        
        if (user.getVotingCode() != null) {
            status.put("code", user.getVotingCode());
            status.put("isUsed", user.isVotingCodeUsed());
            status.put("expiresAt", user.getVotingCodeExpiresAt());
            status.put("isExpired", user.getVotingCodeExpiresAt().isBefore(LocalDateTime.now()));
            status.put("isValid", !user.isVotingCodeUsed() && 
                                 user.getVotingCodeExpiresAt().isAfter(LocalDateTime.now()));
        }

        return status;
    }

    @Transactional
    public User generateCode(String admissionNumber) {
        User user = userRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new VotingException("User not found with admission number: " + admissionNumber));
        
        // Only check for expiration, not for usage
        if (user.getVotingCode() != null && 
            user.getVotingCodeExpiresAt().isAfter(LocalDateTime.now())) {
            throw new VotingException("User already has an active voting code");
        }

        // Generate new code
        String code = generateUniqueHexCode();
        
        // Update user with new code
        user.setVotingCode(code);
        user.setVotingCodeExpiresAt(LocalDateTime.now().plusHours(24));
        user.setVotingCodeUsed(false); // This field becomes irrelevant
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
                // Removed the isVotingCodeUsed check
                .filter(user -> user.getVotingCodeExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public void markCodeAsUsed(String admissionNumber, String code) {
        // Remove this method as we don't mark codes as used anymore
        // Or keep it empty if other parts of the code expect it to exist
    }
}
