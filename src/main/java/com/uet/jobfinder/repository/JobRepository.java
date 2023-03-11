package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
