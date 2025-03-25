package com.voting.kura.repository;

import com.voting.kura.model.User;
import com.voting.kura.model.VoterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAdmissionNumber(String admissionNumber);
    boolean existsByAdmissionNumber(String admissionNumber);
    boolean existsByVotingCode(String votingCode);
    long countByVoterStatus(VoterStatus voterStatus);
}
