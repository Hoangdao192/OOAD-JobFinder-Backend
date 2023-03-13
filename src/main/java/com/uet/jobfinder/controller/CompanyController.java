package com.uet.jobfinder.controller;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.model.CompanyContext;
import com.uet.jobfinder.model.CompanyModel;
import com.uet.jobfinder.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @PostMapping(path = "/{id}")
    public ResponseEntity<Company> createCompany(@PathVariable Long id,
            @RequestBody @Valid CompanyContext companyAddressContext) {
        return ResponseEntity.ok().body(companyService.createCompany(id, companyAddressContext.getCompanyModel(), companyAddressContext.getAddressModel()));
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<CompanyModel>> getAllCompany() {
        return ResponseEntity.ok().body(companyService.getAllCompany());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(companyService.getCompanyById(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CompanyContext> putCompanyById(@PathVariable Long id, @RequestBody @Valid CompanyContext companyContext) {
        return ResponseEntity.ok().body(companyService.putCompanyById(id, companyContext));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(companyService.deleteCompanyById(id));
    }
}
