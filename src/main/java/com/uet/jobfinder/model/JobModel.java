package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobModel {
    private Long id;
    private Long userId;

    private CompanyModel company;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String jobTitle;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String jobDescription;

    @NotNull(message = "SVERR4")
    private AddressModel jobAddress;

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

    private JobStatus status;
    private LocalDate openDate;
    @NotNull(message = "SVERR4")
    private LocalDate closeDate;
}
