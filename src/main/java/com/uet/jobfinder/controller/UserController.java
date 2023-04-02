package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.model.UserModel;
import com.uet.jobfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<PageQueryModel<UserModel>> getAllUser(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(
                userService.getAllUser(page, pageSize)
        );
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping(path = "statistic")
    public ResponseEntity getUserGrowthStatistic(
            @RequestParam(defaultValue = "0", required = false) Integer month,
            @RequestParam Integer year
    ) {
        return ResponseEntity.ok(userService.getUserGrowthStatistic(month, year));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping(path = "count")
    public ResponseEntity getUserNumberStatistic() {
        return ResponseEntity.ok(userService.getUserNumberStatistic());
    }

}
