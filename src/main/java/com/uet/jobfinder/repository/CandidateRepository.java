package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByUser(User user);
}
