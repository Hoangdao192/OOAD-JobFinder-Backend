package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyModel {
    @NotNull(message = "Tên công ty không được là null")
    @NotEmpty(message = "Tên công ty không được để trống")
    private String companyName;
    @NotNull(message = "Logo công ty không được là null")
    @NotEmpty(message = "Logo công ty không được để trống")
    private String companyLogo;
    @NotNull(message = "Mô tả công ty không được là null")
    @NotEmpty(message = "Mô tả công ty không được để trống")
    private String companyDescription;
    @NotNull(message = "Số lượng nhân viên công ty không được là null")
    @NotEmpty(message = "Số lượng nhân viên công ty không được để trống")
    private String numberOfEmployee;
}
