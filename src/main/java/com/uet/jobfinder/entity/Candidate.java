package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppFile avatar;

    private String fullName;
    private String sex;
    private LocalDate dateOfBirth;
    private String contactEmail;
    private String phoneNumber;
    private String selfDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Major> interestedMajors = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<JobApplication> jobApplications;
}
