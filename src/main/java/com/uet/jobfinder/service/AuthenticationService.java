package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.model.ConfirmValidationKeyModel;
import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.repository.ValidationKeyRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JsonWebTokenProvider jwtProvider;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    private ValidationKeyService validationKeyService;
    @Autowired
    private ValidationKeyRepository validationKeyRepository;
    @Autowired
    private EmailService emailService;

    public User register(RegisterRequestModel registerRequestModel) {
        if (userRepository.findByEmail(registerRequestModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        User user = new User(
                registerRequestModel.getEmail(),
                passwordEncoder.encode(registerRequestModel.getPassword())
        );
//        user.addRole(
//                roleRepository.findByName(registerRequestModel.getRole())
//                        .orElseThrow(() -> new IllegalArgumentException("role này không hợp lệ."))
//        );
        user = userRepository.save(user);
        userRepository.flush();
        ValidationKey validationKey = validationKeyService.createNewValidationKey(user);
        emailService.sendEmail(user.getEmail(), validationKey.getValidationKey().toString());
        return user;
    }

    public Boolean confirmRegister(ConfirmValidationKeyModel confirmValidationKeyModel) {
        User user = userRepository.findByEmail(confirmValidationKeyModel.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("user with email %s not exists.",
                                confirmValidationKeyModel.getEmail())
                ));
        ValidationKey validationKey = validationKeyRepository.findByUserAndValidationKey(
                user, confirmValidationKeyModel.getConfirmationKey()
        ).orElseThrow(() -> new IllegalArgumentException(
                "Incorrect validation key."
        ));

        if (LocalDateTime.now().equals(validationKey.getExpirationDate())) {
            return false;
        }
        validationKey.setActivated(true);
        validationKeyRepository.save(validationKey);
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public Map<String, String> login(LoginRequestModel loginRequestModel) {
        User user = userRepository.findByEmail(loginRequestModel.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại trong hệ thống"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestModel.getEmail(),
                        loginRequestModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = jwtProvider.generateToken((User) authentication.getPrincipal());
        Map<String, String> returnData = new HashMap<>();
        returnData.put("accessToken", jwt);
        returnData.put("tokenType", "Bearer");
        return returnData;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtProvider(JsonWebTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Autowired
    public void setValidationKeyService(ValidationKeyService validationKeyService) {
        this.validationKeyService = validationKeyService;
    }
}
