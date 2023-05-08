package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.UserRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestLogRepository extends JpaRepository<UserRequestLog, Long> {
}
