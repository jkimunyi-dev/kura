package com.voting.kura.service;

import com.voting.kura.model.Candidate;
import com.voting.kura.model.Position;
import com.voting.kura.model.User;
import com.voting.kura.util.CourseCodeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {
    private final UserService userService;
    private final PositionService positionService;

    @Autowired
    public EligibilityService(UserService userService, PositionService positionService) {
        this.userService = userService;
        this.positionService = positionService;
    }

    /**
     * Determines if a user is eligible to vote for a specific candidate
     *
     * @param userId The ID of the user who wants to vote
     * @param positionId The ID of the position the candidate is running for
     * @return true if the user is eligible to vote for this position
     */
    public boolean isEligibleToVote(Long userId, Long positionId) {
        User user = userService.getUserById(userId);
        Position position = positionService.getPositionById(positionId);

        return switch (position.getPositionLevel()) {
            case 1 -> isEligibleForClassRepVoting(user, position);
            case 2 -> isEligibleForFacultyRepVoting(user, position);
            case 3 -> isEligibleForUniversityWideVoting(user);
            default -> false;
        };
    }

    private boolean isEligibleForClassRepVoting(User user, Position position) {
        return true; // Implement class-level voting logic
    }

    private boolean isEligibleForFacultyRepVoting(User user, Position position) {
        return true; // Implement faculty-level voting logic
    }

    private boolean isEligibleForUniversityWideVoting(User user) {
        return user.isNumberTwo(); // Only number twos can vote
    }

    /**
     * Determines if a user is eligible to run for a specific position
     *
     * @param user The user who wants to be a candidate
     * @param position The position they want to run for
     * @return true if the user is eligible to be a candidate for this position
     */
    public boolean isEligibleToBeCandidate(User user, Position position) {
        // Add any specific eligibility rules for candidates
        // For now, all users can be candidates for any position
        return true;
    }
}