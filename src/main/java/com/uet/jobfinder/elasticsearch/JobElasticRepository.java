package com.uet.jobfinder.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobElasticRepository extends ElasticsearchRepository<JobElastic, Long> {

    Page<JobElastic> findAllByJobTitle(String jobTitle, Pageable pageable);

}
