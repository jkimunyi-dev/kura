package com.voting.kura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "voting_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotingCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @Column(name = "admission_number", nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private boolean used = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}