package com.uet.jobfinder.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationModel {

    private Long id;
    
    @NotNull(message = "SVERR4")
    private Long candidateId;

    @NotNull(message = "SVERR4")
    private Long jobId;

    private String status;
    private String description;
    
    private MultipartFile cvFile;
    private String cv;

    private LocalDateTime appliedDate;
    private LocalDateTime updatedDate;
}