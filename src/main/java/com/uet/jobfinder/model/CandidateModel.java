package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateModel {
    @NotNull(message = "Tên ứng viên không được là null")
    @NotEmpty(message = "Tên ứng viên không được để trống")
    private String fullName;
    @NotNull(message = "Giới tính ứng viên không được là null")
    @NotEmpty(message = "Giới tính ứng viên không được để trống")
    private String sex;
    @NotNull(message = "Ngày sinh ứng viên không được là null")
    private Date dateOfBirth;
    @NotNull(message = "Email ứng viên không được là null")
    @NotEmpty(message = "Email ứng viên không được để trống")
//    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", message = "Email không hợp lệ.")
    private String contactEmail;
    @NotNull(message = "Số điện thoại ứng viên không được là null")
    @NotEmpty(message = "Số điện thoại ứng viên không được để trống")
//    @Pattern(regexp = "/^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$/im", message = "Số điện thoại không hợp lệ.")
    //1234567890
    private String phoneNumber;
    @NotNull(message = "Mô tả bản thân ứng viên không được là null")
    @NotEmpty(message = "Mô tả bản thân ứng viên không được để trống")
    private String selfDescription;
    @NotNull(message = "Kinh nghiệm ứng viên không được là null")
    @NotEmpty(message = "Kinh nghiệm ứng viên không được để trống")
    private String experience;
    @NotNull(message = "Cơ sở giáo dục không được là null")
    @NotEmpty(message = "Cơ sở giáo dục không được để trống")
    private String education;
}
