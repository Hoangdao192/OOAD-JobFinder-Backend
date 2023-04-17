package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_application_log")
@Builder
public class JobApplicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jobApplicationId;
    private Long jobId;
    private Long candidateId;
    private String description;
    private Long cvFile;
    private String status;
    private LocalDateTime appliedDate;
    private LocalDateTime updatedDate;
    private LocalDateTime logDateTime;
    private String activity;
}
