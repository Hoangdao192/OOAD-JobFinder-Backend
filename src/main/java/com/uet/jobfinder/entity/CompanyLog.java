package com.uet.jobfinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "company_log")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String companyName;
    private Long companyLogo;
    private String companyDescription;
    private Long addressId;
    private String numberOfEmployee;
    private LocalDateTime logDateTime;
    private String activity;
}