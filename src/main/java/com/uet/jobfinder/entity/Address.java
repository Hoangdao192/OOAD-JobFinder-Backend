package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String province;
    private String district;
    private String ward;
    private String detail_address;
    private Float longitude;
    private Float latitude;
//    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Employee employee;
}
