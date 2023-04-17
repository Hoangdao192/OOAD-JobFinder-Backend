package com.uet.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidateLoginResponse extends UserLoginResponse {
    private CandidateDTO candidate;

    public CandidateLoginResponse(UserDTO user, JsonWebTokenDTO token, CandidateDTO candidate) {
        super(user, token);
        this.candidate = candidate;
    }
}
