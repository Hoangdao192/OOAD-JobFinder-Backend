package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_application")
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Job job;

    @ManyToOne
    private Candidate candidate;

    private String description;

    @OneToOne
    private AppFile cvFile;

    private String status;

    private LocalDateTime appliedDate;
    private LocalDateTime updatedDate;
}
