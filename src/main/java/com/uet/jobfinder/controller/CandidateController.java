package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.CandidateContext;
import com.uet.jobfinder.model.CandidateModel;
import com.uet.jobfinder.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

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
    public ResponseEntity<CandidateModel> putCandidateById(
            @ModelAttribute CandidateModel candidateModel,
            HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(
                candidateService.updateCandidate(candidateModel, request)
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> deleteCandidateById(
            @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", candidateService.deleteCandidateById(id, request)));
    }
}
