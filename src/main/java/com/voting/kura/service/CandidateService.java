package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.Candidate;
import com.voting.kura.model.Position;
import com.voting.kura.model.User;
import com.voting.kura.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserService userService;
    private final EligibilityService eligibilityService;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository,
                            UserService userService,
                            EligibilityService eligibilityService) {
        this.candidateRepository = candidateRepository;
        this.userService = userService;
        this.eligibilityService = eligibilityService;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new VotingException("Candidate not found with id: " + id));
    }

    public List<Candidate> getCandidatesByPosition(Long positionId) {
        return candidateRepository.findByPositionId(positionId);
    }

    public List<Candidate> getCandidatesByFaculty(String facultyCode) {
        return candidateRepository.findByFacultyCode(facultyCode);
    }

    public List<Candidate> getCandidatesByDepartmentAndFaculty(String facultyCode, String departmentCode) {
        return candidateRepository.findByFacultyCodeAndDepartmentCode(facultyCode, departmentCode);
    }

    public Candidate registerCandidate(Long userId, Long positionId, String manifesto) {
        User user = userService.getUserById(userId);
        Position position = new Position();
        position.setId(positionId);

        // Check if user is eligible to be a candidate for this position
        if (!eligibilityService.isEligibleToBeCandidate(user, position)) {
            throw new VotingException("User is not eligible to be a candidate for this position");
        }

        Candidate candidate = new Candidate();
        candidate.setUser(user);
        candidate.setPosition(position);
        candidate.setFacultyCode(user.getFacultyCode());
        candidate.setDepartmentCode(user.getDepartmentCode());
        candidate.setManifesto(manifesto);
        candidate.setActive(true);

        return candidateRepository.save(candidate);
    }

    public void deactivateCandidate(Long id) {
        Candidate candidate = getCandidateById(id);
        candidate.setActive(false);
        candidateRepository.save(candidate);
    }
}