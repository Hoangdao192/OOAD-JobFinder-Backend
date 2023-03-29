package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.SavedJobModel;
import com.uet.jobfinder.service.SavedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/job/save")
@PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Candidate')")
public class SavedJobController {

    private SavedJobService savedJobService;

    @GetMapping
    public ResponseEntity<PageQueryModel<SavedJobModel>> getAllJob(
            @RequestParam Long candidateId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                savedJobService.getAllSavedJob(
                        page, pageSize, candidateId, request)
        );
    }

    @PostMapping
    public ResponseEntity<SavedJobModel> saveJob(
            @RequestBody @Valid SavedJobModel savedJobModel,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                savedJobService.candidateSaveJob(savedJobModel, request)
        );
    }

    @DeleteMapping("{savedJobId}")
    public ResponseEntity<Map<String, Object>> unSaveJob(
            @PathVariable Long savedJobId, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", savedJobService.deleteSavedJob(savedJobId, request))
        );
    }

    @Autowired
    public void setSavedJobService(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }
}
