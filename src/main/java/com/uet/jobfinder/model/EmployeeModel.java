package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
    private String fullName;
    private String sex;
    private Address address;
    private Date dateOfBirth;
    private String contactEmail;
    private String phoneNumber;
    private String selfDescription;
    private String experience;
    private String education;
}
