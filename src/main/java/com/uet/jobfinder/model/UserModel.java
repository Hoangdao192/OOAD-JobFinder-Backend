package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel{

    private Long id;
    private String email;
    private Boolean enabled = false;
    private Boolean locked = false;
    private List<Role> roles = new ArrayList<>();

    public UserModel(String email, Boolean enabled, Boolean locked) {
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
