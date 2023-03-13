package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.CandidateContext;
import com.uet.jobfinder.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

    @PostMapping(path = "/{id}")
    public ResponseEntity<CandidateContext> createCandidate(@PathVariable Long id,
                                                           @RequestBody @Valid CandidateContext candidateContext) {
        return ResponseEntity.ok().body(candidateService.createCandidate(id, candidateContext.getCandidateModel(), candidateContext.getAddressModel()));
    }

    @GetMapping
    public ResponseEntity<List<CandidateContext>> getAllCandidate() {
        return ResponseEntity.ok().body(candidateService.getAllCandidate());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CandidateContext> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok().body(candidateService.getCandidateById(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CandidateContext> putCandidateById(@PathVariable Long id,
                                                             @RequestBody @Valid CandidateContext candidateContext) {
        return ResponseEntity.ok().body(candidateService.putCandidateById(id, candidateContext));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok().body(candidateService.deleteCandidateById(id));
    }
}
