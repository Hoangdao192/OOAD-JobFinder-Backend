package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestModel {

    @NotEmpty(message = "Email không được để trống.")
    @NotNull(message = "Email không được là null.")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống.")
    @NotNull(message = "Mật khẩu không được là null.")
    private String password;

}
