package com.voting.kura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String facultyCode;

    @Column(nullable = false)
    private String departmentCode;

    @Column(nullable = false)
    private String admissionYear;

    @Column(nullable = false)
    private String sequentialNumber;

    private boolean isNumberTwo = false;

    private String email;

    // New voter-related fields
    @Column(length = 6)
    private String votingCode;

    private LocalDateTime votingCodeExpiresAt;

    private boolean votingCodeUsed = false;

    private boolean isVoter = false;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private VoterStatus voterStatus = VoterStatus.PENDING;

    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}