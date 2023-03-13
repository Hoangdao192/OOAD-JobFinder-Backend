package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import com.uet.jobfinder.service.JobService;
import com.uet.jobfinder.util.JsonWebTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "job")
@PreAuthorize("isAuthenticated()")
public class JobController {

    @Autowired
    private JobService jobService;
    private JsonWebTokenUtil jsonWebTokenUtil = new JsonWebTokenUtil();
    @Autowired
    private JsonWebTokenProvider jsonWebTokenProvider;

    @PostMapping(path = "create")
    @PreAuthorize("hasAnyAuthority('Company')")
    public ResponseEntity<JobModel> createJob(@RequestBody @Valid JobModel jobModel, HttpServletRequest request) {
        String jwtToken = jsonWebTokenUtil.getJWTFromRequest(request);
        Long userId = Long.parseLong(jsonWebTokenProvider.getUserIdFromJWT(jwtToken));
        jobModel.setUserId(userId);
        return ResponseEntity.ok(jobService.createJob(jobModel));
    }

    @PutMapping(path = "update")
    @PreAuthorize("hasAnyAuthority('Company', 'Admin')")
    public ResponseEntity<JobModel> updateJob(@RequestBody @Valid JobModel jobModel) {
        return ResponseEntity.ok(jobService.updateJob(jobModel));
    }

    @DeleteMapping(path = "delete")
    @PreAuthorize("hasAnyAuthority('Company', 'Admin')")
    public ResponseEntity deleteJob(@RequestParam Long jobId, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", jobService.deleteJob(jobId, request))
        );
    }
}
