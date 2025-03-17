package com.voting.kura.repository;

import com.voting.kura.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByPositionName(String positionName);
    List<Position> findByPositionLevel(Integer positionLevel);
}