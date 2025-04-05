package com.voting.kura.controller;

import com.voting.kura.dto.request.VoteRequest;
import com.voting.kura.dto.response.VoteResponse;
import com.voting.kura.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteResponse> castVote(
            @RequestParam String voterAdmissionNumber,
            @RequestParam String candidateAdmissionNumber,
            @RequestParam String votingCode) {
        return ResponseEntity.ok(voteService.castVote(new VoteRequest(
            voterAdmissionNumber, 
            candidateAdmissionNumber, 
            votingCode
        )));
    }
}
