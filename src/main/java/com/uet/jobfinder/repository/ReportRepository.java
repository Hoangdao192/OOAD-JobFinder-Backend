package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Report;
import com.uet.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<List<Report>> findByUser(User user);
}
