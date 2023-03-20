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
import java.util.List;
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

    @GetMapping
    public ResponseEntity<PageQueryModel> getAllCompany(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(companyService.listCompany(page, pageSize));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(companyService.getCompanyById(id));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Company')")
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
    @PreAuthorize("hasAnyAuthozity('Admin', 'Company')")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", companyService.deleteCompanyById(id, request))
        );
    }
}
