package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @NotNull(message = "Old password is required")
    @NotEmpty(message = "Old password is required")
    public String oldPassword;
    @NotNull(message = "Old password is required")
    @NotEmpty(message = "Old password is required")
    public String newPassword;
}
