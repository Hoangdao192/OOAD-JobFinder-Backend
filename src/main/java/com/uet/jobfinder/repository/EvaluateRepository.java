package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Evaluate;
import com.uet.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluateRepository extends JpaRepository<Evaluate, Long> {
    List<Evaluate> findAllByCompany(Company company);
    boolean existsByCompanyAndUser(Company company, User user);
    Optional<Evaluate> findByCompanyAndUser(Company company, User user);
}
