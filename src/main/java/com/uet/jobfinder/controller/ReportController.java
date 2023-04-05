package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.ReportModel;
import com.uet.jobfinder.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {
    private ReportService reportService;

    @GetMapping
//    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<List<ReportModel>> getAllReport() {
        return ResponseEntity.ok().body(reportService.getAllReport());
    }

//    @GetMapping
////    @PreAuthorize("hasAnyAuthority('Candidate', 'Company')")
//    public ResponseEntity<List<ReportModel>> getAllReportByCompanyOrCandidate(HttpServletRequest request) {
//        return ResponseEntity.ok().body(reportService.getAllReportByCompanyOrCandidate(request));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportModel> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok().body(reportService.getReportById(id));
    }

    @PostMapping
    public ResponseEntity<ReportModel> createReport(@RequestBody ReportModel reportModel) {
        return ResponseEntity.ok().body(reportService.createReport(reportModel));
    }

    @PutMapping
    public ResponseEntity<ReportModel> updateReport(@RequestBody ReportModel reportModel) {
        return ResponseEntity.ok().body(reportService.createReport(reportModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pair<String, ReportModel>> deleteReportById(@PathVariable Long id) {
        return ResponseEntity.ok().body(reportService.deleteReportById(id));
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
