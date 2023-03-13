package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;

    public Company getCompanyByUserId(Long userId) {
        //  TODO: Replace by find company
        User user = userService.getUserById(userId);
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.COMPANY_NOT_EXISTS
                ));
    }

}
