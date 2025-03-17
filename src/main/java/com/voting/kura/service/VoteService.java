package com.voting.kura.service;

import com.voting.kura.dto.request.VoteRequest;
import com.voting.kura.dto.response.VoteResponse;
import com.voting.kura.exception.VotingException;
import com.voting.kura.model.Candidate;
import com.voting.kura.model.Position;
import com.voting.kura.model.User;
import com.voting.kura.model.Vote;
import com.voting.kura.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserService userService;
    private final CandidateService candidateService;
    private final EligibilityService eligibilityService;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       UserService userService,
                       CandidateService candidateService,
                       EligibilityService eligibilityService) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.candidateService = candidateService;
        this.eligibilityService = eligibilityService;
    }

    @Transactional
    public VoteResponse castVote(VoteRequest voteRequest) {
        User voter = userService.getUserById(voteRequest.getVoterId());
        Candidate candidate = candidateService.getCandidateById(voteRequest.getCandidateId());
        Position position = candidate.getPosition();

        // Check if user has already voted for this position
        if (voteRepository.existsByVoterIdAndPositionId(voter.getId(), position.getId())) {
            throw new VotingException("You have already voted for this position");
        }

        // Check if user is eligible to vote for this candidate
        if (!eligibilityService.isEligibleToVote(voter.getId(), position.getId())) {
            throw new VotingException("You are not eligible to vote for this candidate");
        }

        // Check if candidate is active
        if (!candidate.isActive()) {
            throw new VotingException("This candidate is no longer in the race");
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setPosition(position);

        voteRepository.save(vote);

        return new VoteResponse(
                voter.getId(),
                candidate.getId(),
                position.getId(),
                "Vote cast successfully"
        );
    }
}