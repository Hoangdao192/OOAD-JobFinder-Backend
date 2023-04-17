package com.uet.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private Boolean enabled = false;
    private Boolean locked = false;
    private List<String> roles = new ArrayList<>();

    public UserDTO(String email, Boolean enabled, Boolean locked) {
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
    }
}
