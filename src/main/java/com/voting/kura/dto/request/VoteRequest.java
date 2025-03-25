package com.voting.kura.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {
    private String voterAdmissionNumber;
    private String candidateAdmissionNumber;
    private String votingCode;
}
