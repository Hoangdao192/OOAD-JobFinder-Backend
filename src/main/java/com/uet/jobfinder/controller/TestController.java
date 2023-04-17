package com.uet.jobfinder.controller;

import com.uet.jobfinder.dto.JobDTO;
import com.uet.jobfinder.repository.JobRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @GetMapping
    public ResponseEntity testFindJob() {
        return ResponseEntity.ok(
                jobRepository.searchJob(
                         "java", -1L, "", "", 0, 3
                ).stream().map(job -> modelMapper.map(job, JobDTO.class))
        );
    }

}
