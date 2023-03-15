package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestModel {

    @NotEmpty(message = "LGERR2")
    @NotNull(message = "LGERR3")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "AUERR2")
    private String email;

    @NotEmpty(message = "LGERR4")
    @NotNull(message = "LGERR5")
    private String password;

}
