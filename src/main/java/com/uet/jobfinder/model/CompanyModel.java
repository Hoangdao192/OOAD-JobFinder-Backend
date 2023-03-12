package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyModel {
    private String companyName;
    private String companyLogo;
    private String companyDescription;
    private Address address;
    private Integer numberOfEmployee;
}
