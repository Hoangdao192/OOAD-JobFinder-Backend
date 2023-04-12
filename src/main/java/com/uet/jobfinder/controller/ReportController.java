package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.ReportModel;
import com.uet.jobfinder.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("report")
@PreAuthorize("isAuthenticated()")
public class ReportController {
    private ReportService reportService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<PageQueryModel<ReportModel>> getAllReport(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(reportService.getAllReport(
                page, pageSize
        ));
    }

//    @GetMapping(params = {"page", "pageSize", "companyId"})
//    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
//    public ResponseEntity<PageQueryModel<ReportModel>> companyGetAllReport(
//            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
//            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
//            @RequestParam Long companyId, HttpServletRequest request
//    )    {
//        return ResponseEntity.ok(reportService.getAllReportByCompany(
//                page, pageSize, companyId, request
//        ));
//    }

//    @GetMapping(params = {"page", "pageSize", "candidateId"})
//    @PreAuthorize("hasAnyAuthority('Admin', 'Candidate')")
//    public ResponseEntity<PageQueryModel<ReportModel>> candidateGetAllReport(
//            @RequestParam(defaultValue = "0", required = false) Integer page,
//            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
//            @RequestParam Long candidateId, HttpServletRequest request
//    )    {
//        return ResponseEntity.ok(reportService.getAllReportByCandidate(
//                page, pageSize, candidateId, request
//        ));
//    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<ReportModel> getReportById(
            @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.getReportById(id, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Candidate')")
    public ResponseEntity<ReportModel> createReport(
            @RequestBody ReportModel reportModel, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.createReport(reportModel, request));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Candidate')")
    public ResponseEntity<ReportModel> updateReport(
            @RequestBody ReportModel reportModel, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.createReport(reportModel, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Candidate')")
    public ResponseEntity<Pair<String, Boolean>> deleteReportById(
            @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.deleteReportById(id, request));
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
