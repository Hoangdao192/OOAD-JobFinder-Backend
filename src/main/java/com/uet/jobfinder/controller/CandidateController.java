package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.CandidateContext;
import com.uet.jobfinder.model.CandidateModel;
import com.uet.jobfinder.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/candidate")
@PreAuthorize("isAuthenticated()")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

//    @PostMapping(path = "/{id}")
//    public ResponseEntity<CandidateContext> createCandidate(@PathVariable Long id,
//                                                           @RequestBody @Valid CandidateContext candidateContext) {
//        return ResponseEntity.ok().body(candidateService.createCandidate(id, candidateContext.getCandidateModel(), candidateContext.getAddressModel()));
//    }

    @GetMapping
    public ResponseEntity<List<CandidateContext>> getAllCandidate() {
        return ResponseEntity.ok().body(candidateService.getAllCandidate());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CandidateModel> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok().body(candidateService.getCandidateModelById(id));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
    public ResponseEntity<CandidateModel> putCandidateById(
            @ModelAttribute @Valid CandidateModel candidateModel,
            HttpServletRequest request) throws IOException {
        System.out.println(candidateModel.getCandidateAvatarFile().getOriginalFilename());
        return ResponseEntity.ok(
                candidateService.updateCandidate(candidateModel, request)
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
}
