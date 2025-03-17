package com.voting.kura.repository;

import com.voting.kura.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByVoterIdAndPositionId(Long voterId, Long positionId);

    @Query("SELECT v.candidate.id, COUNT(v) as count FROM Vote v WHERE v.position.id = :positionId GROUP BY v.candidate.id ORDER BY count DESC")
    List<Object[]> countVotesByPosition(Long positionId);
}