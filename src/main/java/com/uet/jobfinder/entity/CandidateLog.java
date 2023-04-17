package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate_log")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long avatar;
    private String fullName;
    private String sex;
    private LocalDate dateOfBirth;
    private String contactEmail;
    private String phoneNumber;
    private String selfDescription;
    private LocalDateTime logDateTime;
    private String activity;
}
