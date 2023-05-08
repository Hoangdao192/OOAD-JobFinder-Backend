package com.uet.jobfinder.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "job")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobElastic {
    @Id
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String jobAddress;
    private String major;
    private String salary;
    private Integer numberOfHiring;
    private String requireExperience;
    private String sex;
    private String workingForm;
}
