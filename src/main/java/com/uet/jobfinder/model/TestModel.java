package com.uet.jobfinder.model;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestModel {
    private MultipartFile file;
}
