package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_log")
@Builder
public class JobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jobId;
    private Long companyUserId;
    private String jobTitle;
    private String jobDescription;
    private Long addressId;
    private String major;
    private String salary;
    private Integer numberOfHiring;
    private String requireExperience;
    private String sex;
    private String workingForm;
    private LocalDate openDate;
    private LocalDate closeDate;
    private LocalDateTime logDateTime;
    private String activity;
}
