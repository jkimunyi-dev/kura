package com.voting.kura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(nullable = false)
    private String facultyCode;

    @Column(nullable = false)
    private String departmentCode;

    private String manifesto;

    private boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "vote_count", nullable = false)
    private Integer voteCount = 0;

    public void incrementVoteCount() {
        this.voteCount = this.voteCount + 1;
    }
}
