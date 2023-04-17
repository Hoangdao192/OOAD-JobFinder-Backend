package com.uet.jobfinder.controller;

import com.uet.jobfinder.dto.JobDTO;
import com.uet.jobfinder.dto.PageQueryModel;
import com.uet.jobfinder.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "job")
public class JobController {

    private JobService jobService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping(path = "statistic")
    public ResponseEntity<List<Object>> getUserGrowthStatistic(
            @RequestParam(defaultValue = "0", required = false) Integer month,
            @RequestParam Integer year
    ) {
        return ResponseEntity.ok(jobService.getStatistic(month, year));
    }

    @GetMapping("countAll")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin')")
    public ResponseEntity<Long> countAllJob() {
        return ResponseEntity.ok(
                jobService.countAllJob()
        );
    }

    @GetMapping("count")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Company')")
    public ResponseEntity<Long> countJobByCompanyId(
            @RequestParam Long companyId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                jobService.countOpenJobByCompanyId(companyId, request)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Company') and isAuthenticated()")
    public ResponseEntity<JobDTO> createJob(@RequestBody @Valid JobDTO jobDTO, HttpServletRequest request) {
        return ResponseEntity.ok(jobService.createJob(jobDTO, request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobModelById(id));
    }

    @GetMapping
    public ResponseEntity<PageQueryModel<JobDTO>> getJobList(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer perPage,
            @RequestParam(required = false, defaultValue = "-1") Long companyId,
            @RequestParam(required = false, defaultValue = "") String jobTitle,
            @RequestParam(required = false, defaultValue = "") String major,
            @RequestParam(required = false, defaultValue = "") String workingForm,
            @RequestParam(required = false) Boolean isJobOpen
    ) {
        return ResponseEntity.ok(
                jobService.getAllJob(page, perPage, companyId, jobTitle,
                        major, workingForm, isJobOpen)
        );
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Company', 'Admin') and isAuthenticated()")
    public ResponseEntity<JobDTO> updateJob(@RequestBody @Valid JobDTO jobDTO, HttpServletRequest request) {
        return ResponseEntity.ok(jobService.updateJob(jobDTO, request));
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAnyAuthority('Company', 'Admin') and isAuthenticated()")
    public ResponseEntity<Map<String, Object>> deleteJob(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", jobService.deleteJob(id, request))
        );
    }

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }
}
