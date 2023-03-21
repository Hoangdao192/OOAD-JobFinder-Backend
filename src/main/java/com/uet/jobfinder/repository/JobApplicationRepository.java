package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobAndCandidate(Job job, Candidate candidate);

    Page<JobApplication> findAllByJob(Pageable pageable, Job job);

    Page<JobApplication> findAllByCandidate(Pageable pageable, Candidate candidate);

}
