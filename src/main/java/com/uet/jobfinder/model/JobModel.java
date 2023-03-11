package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobModel {
    @NotNull(message = "Id không được là null.")
    private Long id;

    @NotNull(message = "userId không được là null.")
    private Long userId;

    @NotNull(message = "jobTitle không được là null.")
    @NotEmpty(message = "jobTitle không được để trống.")
    private String jobTitle;

    @NotNull(message = "jobDescription không được là null.")
    @NotEmpty(message = "jobDescription không được để trống.")
    private String jobDescription;

    @NotNull(message = "jobAddress không được là null.")
    @NotEmpty(message = "jobAddress không được để trống.")
    private String jobAddress;

    @NotNull(message = "major không được là null.")
    @NotEmpty(message = "major không được để trống.")
    private String major;

    @NotNull(message = "salary không được là null.")
    @NotEmpty(message = "salary không được để trống.")
    private String salary;

    @NotNull(message = "numberOfHiring không được là null.")
    @Min(value = 0, message = "numberOfHiring không ược nhỏ hơn 0.")
    private Integer numberOfHiring;

    private String requireExperience;
    private String sex;
    private String workingForm;

}
