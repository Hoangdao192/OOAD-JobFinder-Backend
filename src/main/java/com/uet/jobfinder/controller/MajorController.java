package com.uet.jobfinder.controller;

import com.uet.jobfinder.entity.Major;
import com.uet.jobfinder.model.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public ResponseEntity<List<Major>> getAllMajor() {
        return ResponseEntity.ok(majorService.getAllMajor());
    }

}
