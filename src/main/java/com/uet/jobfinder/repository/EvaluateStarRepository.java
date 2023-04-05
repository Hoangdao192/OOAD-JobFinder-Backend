package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.EvaluateStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluateStarRepository extends JpaRepository<EvaluateStar, Long> {
    boolean existsByCompany(Company company);

    boolean existsById(Long id);

    Optional<EvaluateStar> findByCompany(Company company);

    List<EvaluateStar> findEvaluateStarByStarGreaterThan(Byte start);
}
