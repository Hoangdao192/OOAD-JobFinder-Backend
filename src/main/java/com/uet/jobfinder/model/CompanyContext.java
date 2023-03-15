package com.uet.jobfinder.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class CompanyContext {
    @NotNull(message = "Địa chỉ không được là null")
    @Valid
    AddressModel addressModel;
    @NotNull(message = "Công ty không được là null")
    @Valid
    CompanyModel companyModel;
}
