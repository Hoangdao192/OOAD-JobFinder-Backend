package com.uet.jobfinder.model;

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
public class ReportModel {
    private Long companyId;

    private Long userId;

    @NotNull(message = "REPORTEER2")
    @NotEmpty(message = "REPORTEER1")
    private String message;

    private Date date;
}