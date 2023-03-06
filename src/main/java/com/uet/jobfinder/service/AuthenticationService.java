package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User register(RegisterRequestModel registerRequestModel) {
        if (userRepository.findByEmail(registerRequestModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        User user = new User(
                registerRequestModel.getEmail(),
                passwordEncoder.encode(registerRequestModel.getPassword())
        );
        user.addRole(
                roleRepository.findByName(registerRequestModel.getRole())
                        .orElseThrow(() -> new IllegalArgumentException("role này không hợp lệ."))
        );
        user = userRepository.save(user);
        userRepository.flush();
        return user;
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
}
