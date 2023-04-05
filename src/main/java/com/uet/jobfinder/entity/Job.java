package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Company company;
    @Column(nullable = false)
    private String jobTitle;
    @Column(nullable = false)
    private String jobDescription;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address jobAddress;
    @Column(nullable = false)
    private String major;
    @Column(nullable = false)
    private String salary;
    @Column(nullable = false)
    private Integer numberOfHiring;
    private String requireExperience;
    private String sex;
    private String workingForm;

    @Column(nullable = false)
    private LocalDate openDate;
    @Column(nullable = false)
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;
}
