package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
    private String province;
    private String district;
    private String ward;
    private String detail_address;
    private Float longitude;
    private Float latitude;
}
