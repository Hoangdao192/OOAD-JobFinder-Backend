package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.AppFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AppFile, Long> {
}
