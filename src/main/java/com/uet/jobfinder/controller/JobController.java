package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "job")
public class JobController {

    private JobService jobService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Company') and isAuthenticated()")
    public ResponseEntity<JobModel> createJob(@RequestBody @Valid JobModel jobModel, HttpServletRequest request) {
        return ResponseEntity.ok(jobService.createJob(jobModel, request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<JobModel> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobModelById(id));
    }

    @GetMapping
    public ResponseEntity<PageQueryModel<JobModel>> getJobList(
            @RequestParam(defaultValue = "0") Long page,
            @RequestParam(defaultValue = "10") Long perPage
    ) {
        return ResponseEntity.ok(jobService.getAllJob(page, perPage));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Company', 'Admin') and isAuthenticated()")
    public ResponseEntity<JobModel> updateJob(@RequestBody @Valid JobModel jobModel, HttpServletRequest request) {
        return ResponseEntity.ok(jobService.updateJob(jobModel, request));
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
