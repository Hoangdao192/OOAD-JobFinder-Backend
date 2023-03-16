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

    @NotNull(message = "Tên công ty không được là null")
    @NotEmpty(message = "Tên công ty không được để trống")
    private String companyName;

//    @NotNull(message = "Logo công ty không được là null")
//    @NotEmpty(message = "Logo công ty không được để trống")
    private String companyLogo;

    private MultipartFile companyLogoFile;

    @NotNull(message = "Mô tả công ty không được là null")
    @NotEmpty(message = "Mô tả công ty không được để trống")
    private String companyDescription;

    @NotNull(message = "Số lượng nhân viên công ty không được là null")
    @NotEmpty(message = "Số lượng nhân viên công ty không được để trống")
    private String numberOfEmployee;

    private AddressModel address;
}
