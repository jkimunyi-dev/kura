package com.voting.kura.controller;

import com.voting.kura.model.User;
import com.voting.kura.service.VotingCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/voting-code")
public class VotingCodeController {
    private final VotingCodeService votingCodeService;

    @Autowired
    public VotingCodeController(VotingCodeService votingCodeService) {
        this.votingCodeService = votingCodeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateCode(@RequestParam String admissionNumber) {
        User user = votingCodeService.generateCode(admissionNumber);
        return ResponseEntity.ok(Map.of(
            "code", user.getVotingCode(),
            "expiresAt", user.getVotingCodeExpiresAt().toString()
        ));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCode(
            @RequestParam String admissionNumber,
            @RequestParam String code) {
        return ResponseEntity.ok(votingCodeService.validateCode(admissionNumber, code));
    }

    @PostMapping("/use")
    public ResponseEntity<Void> useCode(
            @RequestParam String admissionNumber,
            @RequestParam String code) {
        votingCodeService.markCodeAsUsed(admissionNumber, code);
        return ResponseEntity.ok().build();
    }
}
