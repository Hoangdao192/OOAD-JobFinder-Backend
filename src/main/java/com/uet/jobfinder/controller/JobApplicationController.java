package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.JobApplicationModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("job-application")
@PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Candidate', 'Company')")
public class JobApplicationController {

    private JobApplicationService jobApplicationService;


    @GetMapping("statistic")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity getApplicationStatistic(HttpServletRequest request) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationStatistic(request)
        );
    }

    @GetMapping("statistic/chart")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity getApplicationStatisticChart(
            @RequestParam(defaultValue = "0") Integer month,
            @RequestParam(defaultValue = "2023") Integer year,
            HttpServletRequest request) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationStatisticChart(
                        month, year, request
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
    public ResponseEntity<JobApplicationModel> createApplication(
            @ModelAttribute @Valid JobApplicationModel jobApplicationModel,
            HttpServletRequest request
            ) throws IOException {
        return ResponseEntity.ok(jobApplicationService.createJobApplication(
                jobApplicationModel, request));
    }

    @GetMapping("{applicationId}")
    public ResponseEntity<JobApplicationModel> getJobApplication(
            @PathVariable Long applicationId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                jobApplicationService.getJobApplicationModelById(
                        applicationId, request
                )
        );
    }

    @GetMapping
    public ResponseEntity<PageQueryModel<JobApplicationModel>> listJobApplication(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "-1") Long candidateId,
            @RequestParam(required = false, defaultValue = "-1") Long jobId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(jobApplicationService.listJobApplication(
                page, pageSize, candidateId, jobId, request
        ));
    }

    @GetMapping("listByCompany")
    public ResponseEntity<PageQueryModel<JobApplicationModel>> listJobApplicationByCompanyId(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long companyId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                jobApplicationService.listJobApplicationByCompanyId(
                        page, pageSize,
                        companyId, request
                )
        );
    }

    @GetMapping("count")
    @PreAuthorize("hasAnyAuthority('Company')")
    public ResponseEntity countApplicationByJobId(
            @RequestParam Long jobId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                jobApplicationService.countApplicationByJobId(
                        jobId, request
                )
        );
    }

    // @GetMapping("count")
    // public ResponseEntity count

    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
    @DeleteMapping("{applicationId}")
    public ResponseEntity<Map<String, Object>> deleteJobApplication(
            @PathVariable Long applicationId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
            Map.of(
                "success",
                jobApplicationService.deleteJobApplication(applicationId, request)
            )
        );
    }

    @GetMapping("accept/{applicationId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<Map<String, Object>> acceptApplication(
            @PathVariable Long applicationId, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", jobApplicationService.acceptJobApplication(
                        applicationId, request
                ))
        );
    }

    @GetMapping("reject/{applicationId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<Map<String, Object>> rejectApplication(
            @PathVariable Long applicationId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                Map.of("success", jobApplicationService.rejectJobApplication(
                        applicationId, request
                ))
        );
    }

    @Autowired
    public void setJobApplicationService(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }
}
