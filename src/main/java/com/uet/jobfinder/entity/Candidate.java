package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppFile avatar;

    private String fullName;
    private String sex;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private LocalDate dateOfBirth;
    private String contactEmail;
    private String phoneNumber;
    private String selfDescription;
    private String experience;
    private String education;
}
