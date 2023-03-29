package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

//    @PostMapping(path = "/{id}")
//    public Company createCompany(@PathVariable Long id,
//            @RequestBody @Valid CompanyContext companyAddressContext) {
//        return companyService.createCompany(id, companyAddressContext.getCompanyModel(), companyAddressContext.getAddressModel());
//    }

//    @GetMapping
//    public ResponseEntity<List<CompanyModel>> getAllCompany() {
//        return ResponseEntity.ok().body(companyService.getAllCompany());
//    }

    @GetMapping("/application/count")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Company')")
    public ResponseEntity<Long> countComingApplication(
            @RequestParam Long companyId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                companyService.countComingApplication(companyId,request)
        );
    }


    @GetMapping
    public ResponseEntity<PageQueryModel<CompanyModel>> getAllCompany(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(companyService.getAllCompany(page, pageSize));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(companyService.getCompanyById(id));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<CompanyModel> updateCompany(
            @ModelAttribute @Valid CompanyModel companyModel, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(companyService.updateCompany(companyModel, request));
    }

//    @PutMapping
//    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
//    public ResponseEntity<CompanyModel> updateCompany(
//            @RequestParam("company") String company,
//            HttpServletRequest request) throws IOException {
//
//        System.out.println(company);
//        return ResponseEntity.ok(new CompanyModel());
////        return ResponseEntity.ok(companyService.updateCompany(companyModel, file, request));
//    }

//    public ResponseEntity<CompanyModel> updateCompanyMultipart(@ModelAttribute CompanyModel companyModel) {
//
//    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", companyService.deleteCompanyById(id, request))
        );
    }
}
