package com.uet.jobfinder.controller;

import com.uet.jobfinder.dto.CandidateDTO;
import com.uet.jobfinder.dto.PageQueryModel;
import com.uet.jobfinder.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/candidate")
@PreAuthorize("isAuthenticated()")
public class CandidateController {

    CandidateService candidateService;

    @GetMapping
    public ResponseEntity<PageQueryModel<CandidateDTO>> getAllCandidate(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok().body(candidateService.getAllCandidate(page, pageSize));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok().body(candidateService.getCandidateModelById(id));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
    public ResponseEntity<CandidateDTO> putCandidateById(
            @ModelAttribute @Valid CandidateDTO candidateDTO,
            HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(
                candidateService.updateCandidate(candidateDTO, request)
        );
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
    public ResponseEntity<Map<String, Object>> deleteCandidateById(
            @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", candidateService.deleteCandidateById(id, request)));
    }

    @Autowired
    public void setCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
}
