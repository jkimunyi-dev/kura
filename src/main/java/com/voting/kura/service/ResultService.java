package com.voting.kura.service;

import com.voting.kura.model.Candidate;
import com.voting.kura.repository.CandidateRepository;
import com.voting.kura.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    @Autowired
    public ResultService(VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
    }

    public Map<String, Object> getResultsByPosition(Long positionId) {
        List<Object[]> voteCounts = voteRepository.countVotesByPosition(positionId);
        List<Candidate> candidates = candidateRepository.findByPositionId(positionId);

        Map<Long, String> candidateNames = candidates.stream()
                .collect(Collectors.toMap(
                        Candidate::getId,
                        candidate -> candidate.getUser().getFullName()
                ));

        Map<String, Object> results = new HashMap<>();
        results.put("positionId", positionId);

        List<Map<String, Object>> candidateResults = voteCounts.stream()
                .map(result -> {
                    Long candidateId = (Long) result[0];
                    Long voteCount = (Long) result[1];

                    Map<String, Object> candidateResult = new HashMap<>();
                    candidateResult.put("candidateId", candidateId);
                    candidateResult.put("candidateName", candidateNames.getOrDefault(candidateId, "Unknown"));
                    candidateResult.put("voteCount", voteCount);

                    return candidateResult;
                })
                .collect(Collectors.toList());

        results.put("candidates", candidateResults);
        results.put("totalVotes", candidateResults.stream()
                .mapToLong(result -> (Long) result.get("voteCount"))
                .sum());

        return results;
    }
}