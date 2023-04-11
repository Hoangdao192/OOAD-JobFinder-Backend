package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobApplication;
import com.uet.jobfinder.presentation.JobApplicationMonthStatisticPresentation;
import com.uet.jobfinder.presentation.JobApplicationYearStatisticPresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobAndCandidate(Job job, Candidate candidate);

    Page<JobApplication> findAllByJob(Pageable pageable, Job job);

    Page<JobApplication> findAllByCandidate(Pageable pageable, Candidate candidate);

    @Query("select ja from JobApplication ja " +
            "inner join Job j on ja.job.id = j.id " +
            "inner join Company c on c.id = j.company.id " +
            "where c.id = :companyId")
    Page<JobApplication> findAllByCompany(Pageable pageable, @Param("companyId") Long companyId);

    @Query("select count(ja) from JobApplication ja " +
            "inner join Job j on ja.job.id = j.id " +
            "inner join Company c on c.id = j.company.id " +
            "where c.id = ?1")
    Long countCompanyComingApplication(Long companyId);

    @Query("select count(ja) from JobApplication ja " +
            "inner join Job j on ja.job.id = j.id " +
            "inner join Company c on c.id = j.company.id " +
            "where c.id = ?1 and ja.status <> 'Waiting'")
    Long countCompanyRepliedApplication(Long companyId);

    @Query(nativeQuery = true, value = "select count(*) from job_application " +
            "inner join job on job_application.job_id = job.id " +
            "inner join company on job.company_user_id = company.user_id " +
            "where company.user_id = :companyId and job_application.status = 'Applied'")
    Long countAppliedApplicationByCompanyId(@Param("companyId") Long companyId);

    @Query(nativeQuery = true, value = "select count(*) from job_application " +
            "inner join job on job_application.job_id = job.id " +
            "inner join company on job.company_user_id = company.user_id " +
            "where company.user_id = :companyId and job_application.status = 'Rejected'")
    Long countRejectedApplicationByCompanyId(Long companyId);

    Long countAllByJob(Job job);

    @Query(nativeQuery = true,
            value = "SELECT MONTH(applied_date) as month, count(*) as numberOfApplication FROM jobfinder.job_application " +
            "inner join job on job.id = job_application.job_id " +
            "inner join company on company.user_id = job.company_user_id " +
            "where company.user_id = :companyId and YEAR(applied_date) = :year " +
            "group by MONTH(applied_date)")
    List<JobApplicationYearStatisticPresentation>  getJobApplicationStatisticByYear(@Param("companyId") Long companyId, @Param("year") Integer year);

    @Query(nativeQuery = true,
            value = "SELECT DAY(applied_date) as day, count(*) as numberOfApplication FROM jobfinder.job_application " +
                    "inner join job on job.id = job_application.job_id " +
                    "inner join company on company.user_id = job.company_user_id " +
                    "where company.user_id = :companyId and MONTH(applied_date) = :month and YEAR(applied_date) = :year " +
                    "group by DAY(applied_date)")
    List<JobApplicationMonthStatisticPresentation> getJobApplicationStatisticByMonth(
            @Param("companyId") Long companyId,
            @Param("month") Integer month,
            @Param("year") Integer year);
}
