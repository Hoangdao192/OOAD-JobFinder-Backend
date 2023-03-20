package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyModel {
    private Long userId;

    @NotNull(message = "SVERR4")
    @NotEmpty(message = "SVERR3")
    private String companyName;

//    @NotNull(message = "SVERR4")
//    @NotEmpty(message = "SVERR3")
    private String companyLogo;

    private MultipartFile companyLogoFile;

    private String companyDescription;
    private String numberOfEmployee;

    private AddressModel address;
}
