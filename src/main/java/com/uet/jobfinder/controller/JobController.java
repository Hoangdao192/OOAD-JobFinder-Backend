package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "job")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "creat")
    public ResponseEntity<JobModel> createJob(@RequestBody @Valid JobModel jobModel) {
        JobModel result = new JobModel();
        modelMapper.map(jobService.createJob(jobModel), result);
        return ResponseEntity.ok(result);
    }
}
