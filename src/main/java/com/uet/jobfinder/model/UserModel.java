package com.uet.jobfinder.model;

import com.uet.jobfinder.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel{

    private Long id;
    private String email;
    private String password;
    private Boolean enabled;
    private Boolean locked;
    private List<Role> roles = new ArrayList<>();

    public UserModel(String email, String password, Boolean enabled, Boolean locked) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.locked = locked;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
