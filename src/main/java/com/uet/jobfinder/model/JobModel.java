package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobModel {
    private Long id;
    private Long userId;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String jobTitle;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String jobDescription;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String jobAddress;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String major;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String salary;

    @NotNull(message = "SVERR4")
    @Min(value = 0, message = "numberOfHiring không ược nhỏ hơn 0.")
    private Integer numberOfHiring;

    private String requireExperience;
    private String sex;
    private String workingForm;
}
