package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.EvaluateModel;
import com.uet.jobfinder.model.EvaluateStarModel;
import com.uet.jobfinder.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("evaluate")
@PreAuthorize("isAuthenticated()")
public class EvaluateController {
    private EvaluateService evaluateService;

    @GetMapping("total/{companyId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<EvaluateStarModel> getCompanyTotalEvaluate(
        @PathVariable Long companyId, HttpServletRequest request) {

        return ResponseEntity.ok(evaluateService.getCompanyTotalEvaluate(companyId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<List<EvaluateModel>> findAllEvaluate() {
        List<EvaluateModel> evaluateList = evaluateService.getAllEvaluate();
        return ResponseEntity.ok().body(evaluateList);
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<List<EvaluateModel>> findAllEvaluateByCompany(
            @PathVariable Long companyId, HttpServletRequest request) {
        List<EvaluateModel> evaluateList = evaluateService
                .getAllEvaluateByCompany(companyId, request);
        return ResponseEntity.ok().body(evaluateList);
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('Candidate')")
    public ResponseEntity<EvaluateModel> createEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                evaluateService.createEvaluate(evaluateModel, request)
        );
    }

    @PutMapping
    public ResponseEntity<EvaluateModel> updateEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(
                evaluateService.updateEvaluate(evaluateModel, httpServletRequest));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel,
            HttpServletRequest httpServletRequest) {

        evaluateService.deleteEvaluate(evaluateModel, httpServletRequest);
        return ResponseEntity.ok(
                Map.of("message", "Delete evaluate successfully")
        );
    }

    @Autowired
    public void setEvaluateService(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }
}
