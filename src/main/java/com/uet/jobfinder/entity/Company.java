package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "company")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_logo")
    private String companyLogo;
    @Column(name = "company_description")
    private String companyDescription;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_address_id")
    private Address address;
    @Column(name = "number_of_employee")
    private String numberOfEmployee;
}
