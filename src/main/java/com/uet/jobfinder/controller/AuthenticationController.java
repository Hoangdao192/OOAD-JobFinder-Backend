package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.ConfirmValidationKeyModel;
import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
        return ResponseEntity.ok(authenticationService.confirmRegister(validationKeyModel));
    }

    @GetMapping(path = "register/confirm/resend")
    public ResponseEntity resendRegisterConfirmationEmail(
            @RequestParam
            @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "AUERR1")
            String email) {
        boolean isSuccess = authenticationService.sendEmailVerification(email);
        return ResponseEntity.ok(
                Map.of("success", isSuccess)
        );
    }
}
