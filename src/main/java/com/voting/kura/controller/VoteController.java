package com.voting.kura.controller;

import com.voting.kura.dto.request.VoteRequest;
import com.voting.kura.dto.response.VoteResponse;
import com.voting.kura.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteResponse> castVote(@RequestBody VoteRequest voteRequest) {
        return ResponseEntity.ok(voteService.castVote(voteRequest));
    }
}