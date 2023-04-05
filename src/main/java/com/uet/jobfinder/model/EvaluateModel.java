package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateModel {

    private Long candidateId;
    private Long companyId;
    @NotNull(message = "SVERR4")
    @Min(0)
    @Max(5)
    private Byte star;

    private CandidateModel candidate;
    private CompanyModel company;
}
