package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "company")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userID;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_logo")
    private String companyLogo;
    @Column(name = "company_description")
    private String companyDescription;
    @Column(name = "company_address_id")
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Address address;
    @Column(name = "number_of_employee")
    private Integer numberOfEmployee;
}
