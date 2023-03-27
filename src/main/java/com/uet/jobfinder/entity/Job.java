package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Company company;
    @Column(nullable = false)
    private String jobTitle;
    @Column(nullable = false)
    private String jobDescription;
    @Column(nullable = false)
    private String jobAddress;
    @Column(nullable = false)
    private String major;
    @Column(nullable = false)
    private String salary;
    @Column(nullable = false)
    private Integer numberOfHiring;
    private String requireExperience;
    private String sex;
    private String workingForm;

    private LocalDateTime openDateTime;
    @Min(value = 0, message = "duration must bigger than 0.")
    private Long duration;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "ENUM('OPEN', 'CLOSE')")
    private JobStatus status;
}
