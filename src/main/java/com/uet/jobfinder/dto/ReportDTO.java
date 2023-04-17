package com.uet.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;

    private Long companyId;
    private Long candidateId;

    private CompanyDTO company;
    private CandidateDTO candidate;

    @NotNull(message = "REPORTEER2")
    @NotEmpty(message = "REPORTEER1")
    private String message;

    private Date date;
}