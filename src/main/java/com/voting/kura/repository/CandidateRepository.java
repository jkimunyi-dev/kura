package com.voting.kura.repository;

import com.voting.kura.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUserId(Long userId);
    List<Candidate> findByPositionId(Long positionId);
    List<Candidate> findByFacultyCode(String facultyCode);
    List<Candidate> findByFacultyCodeAndDepartmentCode(String facultyCode, String departmentCode);
}