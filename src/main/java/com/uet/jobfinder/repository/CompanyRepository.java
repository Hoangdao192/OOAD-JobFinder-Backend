package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);

    void deleteById(Long id);
}
