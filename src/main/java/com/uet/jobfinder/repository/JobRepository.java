package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    Long countJobByCompanyAndStatus(Company company, JobStatus status);

//    @Query("select j from Job j where (:companyId is null or j.company.id = :companyId) " +
//            "and (:jobTitle is null or (match (j.jobTitle) against (:jobTitle))) and (:major is null or :major = j.major)")
//    Page<Job> findAll(Pageable pageable, @Param("companyId") Long companyId,
//                      @Param("jobTitle") String jobTitle, @Param("major") String major,
//                      @Param("workingForm") String workingForm
//    );
}
