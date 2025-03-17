package com.voting.kura.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {
    private Long voterId;
    private Long candidateId;
    private Long positionId;
    private String message;
}