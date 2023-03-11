package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.ConfirmValidationKeyModel;
import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
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

    @PostMapping(path = "register/confirm")
    public ResponseEntity confirmRegister(@RequestBody @Valid ConfirmValidationKeyModel validationKeyModel) {
        boolean success = authenticationService.confirmRegister(validationKeyModel);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true));
        }
        return ResponseEntity.ok(Map.of("success", false, "message", "Key is expired."));
    }

}
