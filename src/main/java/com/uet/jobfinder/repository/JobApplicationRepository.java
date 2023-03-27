package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobAndCandidate(Job job, Candidate candidate);

    Page<JobApplication> findAllByJob(Pageable pageable, Job job);

    Page<JobApplication> findAllByCandidate(Pageable pageable, Candidate candidate);

    @Query("select count(ja) from JobApplication ja " +
            "inner join Job j on ja.job.id = j.id " +
            "inner join Company c on c.id = j.company.id " +
            "where c.id = ?1")
    Long countCompanyComingApplication(Long companyId);

}
