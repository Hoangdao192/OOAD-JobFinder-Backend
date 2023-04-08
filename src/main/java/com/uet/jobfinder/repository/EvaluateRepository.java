package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Evaluate;
import com.uet.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EvaluateRepository extends JpaRepository<Evaluate, Long> {
    List<Evaluate> findAllByCompany(Company company);
    boolean existsByCandidateAndCompany(Candidate candidate, Company company);
    Optional<Evaluate> findByCandidateAndCompany(Candidate candidate, Company company);

    @Query(value = "SELECT AVG(star) FROM evaluate where company_id = :companyId",
            nativeQuery = true)
    Double getAverageEvaluateByCompanyId(@Param("companyId") Long companyId);
}
