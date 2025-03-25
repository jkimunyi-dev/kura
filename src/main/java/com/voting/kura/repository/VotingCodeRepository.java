package com.voting.kura.repository;

import com.voting.kura.model.VotingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingCodeRepository extends JpaRepository<VotingCode, Long> {
    Optional<VotingCode> findByAdmissionNumberAndUsedFalse(String admissionNumber);
    Optional<VotingCode> findByCode(String code);
}