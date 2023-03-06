package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path = "login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestModel loginRequestModel) {
        return ResponseEntity.ok(authenticationService.login(loginRequestModel));
    }

    @PostMapping(path = "register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestModel registerRequestModel) {
        return ResponseEntity.ok(authenticationService.register(registerRequestModel));
    }

}
