package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.*;
import com.uet.jobfinder.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Map;

@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping(path = "login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestModel loginRequestModel) {
        return ResponseEntity.ok(authenticationService.login(loginRequestModel));
    }

    @PostMapping(path = "register")
    public ResponseEntity<UserModel> register(@RequestBody @Valid RegisterRequestModel registerRequestModel) {
        return ResponseEntity.ok(authenticationService.register(registerRequestModel));
    }

    @PutMapping(path = "password/change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity changePassword(
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto,
            HttpServletRequest request) {
        authenticationService.changePassword(changePasswordRequestDto, request);
        return ResponseEntity.ok(
                Map.of("success", true)
        );
    }

    @PostMapping(path = "register/confirm")
    public ResponseEntity<Map<String, Object>> confirmRegister(@RequestBody @Valid ConfirmValidationKeyModel validationKeyModel) {
        return ResponseEntity.ok(authenticationService.confirmRegister(validationKeyModel));
    }

    @GetMapping(path = "register/confirm/resend")
    public ResponseEntity<Map<String, Object>> resendRegisterConfirmationEmail(
            @RequestParam
            @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "AUERR1")
            String email) {
        boolean isSuccess = authenticationService.sendEmailVerification(email);
        return ResponseEntity.ok(
                Map.of("success", isSuccess)
        );
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
