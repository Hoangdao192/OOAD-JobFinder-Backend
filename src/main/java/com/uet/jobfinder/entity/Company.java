package com.uet.jobfinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.REFRESH)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppFile companyLogo;

    @Column(columnDefinition = "TEXT")
    private String companyDescription;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    private String numberOfEmployee;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Job> jobList;
}