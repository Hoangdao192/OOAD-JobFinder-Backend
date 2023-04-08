package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobStatus;
import com.uet.jobfinder.presentation.JobMonthStatisticPresentation;
import com.uet.jobfinder.presentation.JobYearStatisticPresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    Long countAllByCompany(Company company);

    Long countJobByCompanyAndStatus(Company company, JobStatus status);

    @Query(nativeQuery = true,
            value = "select count(*) from job where company_user_id = :companyId and " +
                    "((:isOpen = true and close_date >= NOW()) or (:isOpen = false and close_date < NOW()))")
    Long countJobByCompanyIdAndOpenStatus(@Param("companyId") Long companyId, @Param("isOpen") boolean isOpen);

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

    @Query(nativeQuery = true, value =
            "select count(*) as numberOfJob, MONTH(open_date) as month " +
            "from job where YEAR(job.open_date) = :year " +
            "group by Month(open_date)"
    )
    List<JobYearStatisticPresentation> getYearStatistic(@Param("year") Integer year);

    @Query(nativeQuery = true, value =
            "select count(*) as numberOfJob, DAY(open_date) as day " +
                    "from job where YEAR(job.open_date) = :year and MONTH(job.open_date) = :month " +
                    "group by DAY(open_date)"
    )
    List<JobMonthStatisticPresentation> getMonthStatistic(
            @Param("month") Integer month, @Param("year") Integer year
    );
}
