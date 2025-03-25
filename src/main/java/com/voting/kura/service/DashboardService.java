package com.voting.kura.service;

import com.voting.kura.dto.response.CandidateMetrics;
import com.voting.kura.dto.response.CandidateMetricsResponse;
import com.voting.kura.model.Candidate;
import com.voting.kura.model.Position;
import com.voting.kura.model.VoterStatus;
import com.voting.kura.repository.CandidateRepository;
import com.voting.kura.repository.PositionRepository;
import com.voting.kura.repository.UserRepository;
import com.voting.kura.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final PositionRepository positionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Autowired
    public DashboardService(
            PositionRepository positionRepository,
            CandidateRepository candidateRepository,
            VoteRepository voteRepository,
            UserRepository userRepository) {
        this.positionRepository = positionRepository;
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public CandidateMetricsResponse getPositionMetrics(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        List<Candidate> candidates = candidateRepository.findByPositionId(positionId);
        List<Object[]> voteCounts = voteRepository.countVotesByPosition(positionId);
        
        // Calculate total eligible voters for this position
        long totalEligibleVoters = userRepository.countByVoterStatus(VoterStatus.ACTIVE);

        // Create a map of candidate ID to vote count
        Map<Long, Long> voteCountMap = voteCounts.stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> (Long) result[1]
                ));

        long totalVotes = voteCountMap.values().stream().mapToLong(Long::longValue).sum();
        
        List<CandidateMetrics> candidateMetricsList = new ArrayList<>();
        String winningCandidate = "No candidates";
        long maxVotes = 0;

        for (Candidate candidate : candidates) {
            long votes = voteCountMap.getOrDefault(candidate.getId(), 0L);
            double percentage = totalVotes > 0 ? (votes * 100.0) / totalVotes : 0.0;

            if (votes > maxVotes) {
                maxVotes = votes;
                winningCandidate = candidate.getUser().getFullName();
            }

            candidateMetricsList.add(CandidateMetrics.builder()
                    .candidateId(candidate.getId())
                    .candidateName(candidate.getUser().getFullName())
                    .admissionNumber(candidate.getUser().getAdmissionNumber())
                    .facultyCode(candidate.getFacultyCode())
                    .departmentCode(candidate.getDepartmentCode())
                    .manifesto(candidate.getManifesto())
                    .voteCount(votes)
                    .votePercentage(Math.round(percentage * 100.0) / 100.0)
                    .build());
        }

        // Sort candidates by vote count in descending order
        candidateMetricsList.sort(Comparator.comparingLong(CandidateMetrics::getVoteCount).reversed());

        double voterTurnout = totalEligibleVoters > 0 ? 
            (totalVotes * 100.0) / totalEligibleVoters : 0.0;

        return CandidateMetricsResponse.builder()
                .positionId(positionId)
                .positionName(position.getPositionName()) // Changed from getName() to getPositionName()
                .totalCandidates(candidates.size())
                .totalVotes(totalVotes)
                .candidates(candidateMetricsList)
                .winningCandidate(winningCandidate)
                .voterTurnout(Math.round(voterTurnout * 100.0) / 100.0)
                .build();
    }
}