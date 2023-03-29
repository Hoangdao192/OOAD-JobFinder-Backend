package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.ResultSet;

public interface JobRepository extends JpaRepository<Job, Long> {

    Long countJobByCompanyAndStatus(Company company, JobStatus status);

    @Query(nativeQuery = true, value =
            "select * from job where job.id in " +
            "   (select job.id from job where match (job.job_title) against (:jobTitle)) " +
            "   and (:companyId is null or job.company_user_id = :companyId) " +
            "   and (:major is null or job.major = :major) " +
            "   and (:workingForm is null or job.working_form = :workingForm)")
    Page<Job> findAllWithJobTitle(Pageable pageable, @Param("companyId") Long companyId,
                      @Param("jobTitle") String jobTitle, @Param("major") String major,
                      @Param("workingForm") String workingForm
    );

    @Query(nativeQuery = true, value =
            "select * from job where " +
                    "   (:companyId is null or job.company_user_id = :companyId) " +
                    "   and (:major is null or job.major = :major) " +
                    "   and (:workingForm is null or job.working_form = :workingForm)")
    Page<Job> findAllWithOutTitle(Pageable pageable, @Param("companyId") Long companyId,
                                  @Param("major") String major,
                                  @Param("workingForm") String workingForm
    );
}
