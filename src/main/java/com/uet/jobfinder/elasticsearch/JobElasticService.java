package com.uet.jobfinder.elasticsearch;

import com.uet.jobfinder.repository.JobRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JobElasticService {

    @Autowired
    private JobElasticRepository jobElasticRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void createJobIndex(JobElastic jobElastic) {
        jobElasticRepository.save(jobElastic);
//        jobElasticRepository.searchSimilar()
    }

    public void createJobIndexBulk() {
        jobElasticRepository.saveAll(
                jobRepository.findAll()
                        .stream().map(job ->
                                modelMapper.map(job, JobElastic.class))
                        .collect(Collectors.toList())
        );
    }

    public Page<JobElastic> findJobByTitle(String jobTitle) {
        return jobElasticRepository.findAllByJobTitle(jobTitle,
                PageRequest.of(0, 10));
    }

}
