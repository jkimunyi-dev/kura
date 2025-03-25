package com.voting.kura.service;

import com.voting.kura.dto.request.VoteRequest;
import com.voting.kura.dto.response.VoteResponse;
import com.voting.kura.exception.VotingException;
import com.voting.kura.model.*;
import com.voting.kura.repository.CandidateRepository;
import com.voting.kura.repository.UserRepository;
import com.voting.kura.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserService userService;
    private final CandidateService candidateService;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final VotingCodeService votingCodeService;
    private final EligibilityService eligibilityService;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       UserService userService,
                       CandidateService candidateService,
                       UserRepository userRepository,
                       CandidateRepository candidateRepository,
                       VotingCodeService votingCodeService,
                       EligibilityService eligibilityService) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.candidateService = candidateService;
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.votingCodeService = votingCodeService;
        this.eligibilityService = eligibilityService;
    }

    @Transactional
    public VoteResponse castVote(VoteRequest voteRequest) {
        // Get voter by admission number
        User voter = userRepository.findByAdmissionNumber(voteRequest.getVoterAdmissionNumber())
                .orElseThrow(() -> new VotingException("Voter not found with admission number: " + voteRequest.getVoterAdmissionNumber()));

        // Verify voter status
        if (voter.getVoterStatus() != VoterStatus.ACTIVE) {
            throw new VotingException("Voter is not active");
        }

        // Verify voting code
        if (!votingCodeService.validateCode(voteRequest.getVoterAdmissionNumber(), voteRequest.getVotingCode())) {
            throw new VotingException("Invalid or expired voting code");
        }

        // Get candidate's user by admission number
        User candidateUser = userRepository.findByAdmissionNumber(voteRequest.getCandidateAdmissionNumber())
                .orElseThrow(() -> new VotingException("Candidate not found with admission number: " + voteRequest.getCandidateAdmissionNumber()));

        // Find the candidate record
        Candidate candidate = candidateRepository.findByUserId(candidateUser.getId())
                .orElseThrow(() -> new VotingException("No candidate found with admission number: " + voteRequest.getCandidateAdmissionNumber()));

        // Check if candidate is active
        if (!candidate.isActive()) {
            throw new VotingException("This candidate is no longer in the race");
        }

        // Check eligibility
        if (!eligibilityService.isEligibleToVote(voter.getId(), candidate.getPosition().getId())) {
            throw new VotingException("You are not eligible to vote for this position");
        }

        // Check if voter has already voted for this position
        if (voteRepository.existsByVoterIdAndPositionId(voter.getId(), candidate.getPosition().getId())) {
            throw new VotingException("You have already voted for this position");
        }

        // Create and save the vote
        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setPosition(candidate.getPosition());
        voteRepository.save(vote);

        // Mark voting code as used
        votingCodeService.markCodeAsUsed(voteRequest.getVoterAdmissionNumber(), voteRequest.getVotingCode());

        // Increment candidate's vote count
        candidate.incrementVoteCount();
        candidateRepository.save(candidate);

        return new VoteResponse(
                voter.getId(),
                candidate.getId(),
                candidate.getPosition().getId(),
                "Vote cast successfully"
        );
    }
}