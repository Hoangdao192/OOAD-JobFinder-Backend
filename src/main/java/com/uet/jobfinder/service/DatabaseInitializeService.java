package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializeService {

    public DatabaseInitializeService() {

    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        if (roleRepository.findByName(UserType.ADMIN).isEmpty()) {
            roleRepository.save(new Role(UserType.ADMIN));
        }
        if (roleRepository.findByName(UserType.CANDIDATE).isEmpty()) {
            roleRepository.save(new Role(UserType.CANDIDATE));
        }
        if (roleRepository.findByName(UserType.COMPANY).isEmpty()) {
            roleRepository.save(new Role(UserType.COMPANY));
        }
    }
}
