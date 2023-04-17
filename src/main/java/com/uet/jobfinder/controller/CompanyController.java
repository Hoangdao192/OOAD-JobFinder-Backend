package com.uet.jobfinder.controller;

import com.uet.jobfinder.dto.CompanyDTO;
import com.uet.jobfinder.dto.PageQueryModel;
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

    CompanyService companyService;

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
    public ResponseEntity<PageQueryModel<CompanyDTO>> getAllCompany(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(companyService.getAllCompany(page, pageSize));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(companyService.getCompanyById(id));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<CompanyDTO> updateCompany(
            @ModelAttribute @Valid CompanyDTO companyDTO, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(companyService.updateCompany(companyDTO, request));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('Admin', 'Company')")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of("success", companyService.deleteCompanyById(id, request))
        );
    }

    @GetMapping("/find")
    public ResponseEntity<PageQueryModel<CompanyDTO>> findCompany(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String searchKey){
        return ResponseEntity.ok().body(companyService.findCompany(searchKey, page, pageSize));
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
