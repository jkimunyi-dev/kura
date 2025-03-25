package com.voting.kura.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateMetrics {
    private Long candidateId;
    private String candidateName;
    private String admissionNumber;
    private String facultyCode;
    private String departmentCode;
    private String manifesto;
    private long voteCount;
    private double votePercentage;
}
