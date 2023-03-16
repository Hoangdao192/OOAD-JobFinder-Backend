package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    private String sex;

    private Date dateOfBirth;

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "AUERR2")
    private String contactEmail;

//    @Pattern(regexp = "/^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$/im", message = "Số điện thoại không hợp lệ.")
    //1234567890
    private String phoneNumber;

    private String selfDescription;
    private String experience;
    private String education;

    private AddressModel address;

    private String avatar;
    private MultipartFile avatarFile;
}
