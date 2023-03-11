package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private UserRepository userRepository;

    public Company getCompanyByUserId(Long userId) {
        //  TODO: Replace by find company
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("userId not exists.");
        }
        return new Company();
    }

}
