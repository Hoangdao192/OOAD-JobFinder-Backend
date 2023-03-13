package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(name = "fullname")
    private String fullName;
    private String sex;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "self_description")
    private String selfDescription;
    private String experience;
    private String education;
}
