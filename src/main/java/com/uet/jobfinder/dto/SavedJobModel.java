package com.uet.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedJobModel {

    private Long id;
    @NotNull
    private Long jobId;

    private JobDTO job;

    @NotNull
    private Long candidateId;

}
