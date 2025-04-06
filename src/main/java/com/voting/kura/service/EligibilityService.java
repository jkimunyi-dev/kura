package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.Candidate;
import com.voting.kura.model.Position;
import com.voting.kura.model.User;
import com.voting.kura.repository.CandidateRepository;
import com.voting.kura.util.CourseCodeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {
    private final UserService userService;
    private final PositionService positionService;
    private final CandidateRepository candidateRepository;

    @Autowired
    public EligibilityService(UserService userService, PositionService positionService, CandidateRepository candidateRepository) {
        this.userService = userService;
        this.positionService = positionService;
        this.candidateRepository = candidateRepository;
    }

    /**
     * Determines if a user is eligible to vote for a specific candidate
     *
     * @param userId The ID of the user who wants to vote
     * @param positionId The ID of the position the candidate is running for
     * @return true if the user is eligible to vote for this position
     */
    public boolean isEligibleToVote(Long userId, Long positionId) {
        // All users with valid voting codes can vote for any position
        return true;
    }

    /**
     * Determines if a user is eligible to run for a specific position
     *
     * @param user The user who wants to be a candidate
     * @param position The position they want to run for
     * @return true if the user is eligible to be a candidate for this position
     */
    public boolean isEligibleToBeCandidate(User user, Position position) {
        // All users can be candidates for any position
        return true;
    }
}
