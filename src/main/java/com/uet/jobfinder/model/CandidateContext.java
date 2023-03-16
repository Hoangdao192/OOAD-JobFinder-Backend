package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateContext {
    @NotNull(message = "Ứng viên không được là null")
    @Valid
    CandidateModel candidateModel;
    @NotNull(message = "Địa chỉ ứng viên không được là null")
    @Valid
    AddressModel addressModel;
}
