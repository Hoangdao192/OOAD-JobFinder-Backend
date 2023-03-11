package com.uet.jobfinder.model;

import com.uet.jobfinder.validator.RoleConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestModel {

    @NotEmpty(message = "Email không được để trống.")
    @NotNull(message = "Email không được là null.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống.")
    @NotNull(message = "Mật khẩu không được là null.")
    private String password;

    @NotEmpty(message = "Xác nhận mật khẩu không được để trống.")
    @NotNull(message = "Xác nhận mật khẩu không được là null.")
    private String confirmPassword;

    @NotEmpty(message = "role không được để trống.")
    @NotNull(message = "role không được là null.")
    @RoleConstraint(message = "role không hợp lệ.")
    private String role;

}
