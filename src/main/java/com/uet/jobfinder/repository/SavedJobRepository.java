package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    Optional<SavedJob> findByJobAndCandidate(Job job, Candidate candidate);
    boolean existsByJobAndCandidate(Job job, Candidate candidate);

    Page<SavedJob> findAllByCandidate(Pageable pageable, Candidate candidate);

}
