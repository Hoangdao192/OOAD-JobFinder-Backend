package com.uet.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyLoginResponse extends UserLoginResponse {
    private CompanyDTO company;

    public CompanyLoginResponse(UserDTO user, JsonWebTokenDTO token, CompanyDTO company) {
        super(user, token);
        this.company = company;
    }
}
