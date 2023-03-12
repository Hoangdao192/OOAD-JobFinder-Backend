package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User userID;
    @Column(name = "fullname")
    private String fullName;
    private String sex;
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
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
