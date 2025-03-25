package com.voting.kura.dto.response;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class CandidateMetricsResponse {
    private Long positionId;
    private String positionName;
    private int totalCandidates;
    private long totalVotes;
    private List<CandidateMetrics> candidates;
    private String winningCandidate;
    private double voterTurnout;
}

