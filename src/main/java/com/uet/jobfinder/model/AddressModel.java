package com.uet.jobfinder.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressModel {
    @NotNull(message = "Tỉnh/thành phố không được là null")
    @NotEmpty(message = "Tỉnh/thành phố không được để trống")
    private String province;
    @NotNull(message = "Quận/huyện không được là null")
    @NotEmpty(message = "Quận/huyện không được để trống")
    private String district;
    @NotNull(message = "Phường/xã không được là null")
    @NotEmpty(message = "Phường/xã không được để trống")
    private String ward;
    @NotNull(message = "Địa chỉ chi tiết không được là null")
    @NotEmpty(message = "Địa chỉ chi tiết không được để trống")
    private String detailAddress;
    @NotNull(message = "Kinh độ không được là null")
    private Float longitude;
    @NotNull(message = "Vĩ độ không được là null")
    private Float latitude;

}
