package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User createUser(RegisterRequestModel registerRequestModel) {
        if (userRepository.findByEmail(registerRequestModel.getEmail())
                .isPresent()) {
            throw new CustomIllegalArgumentException(ServerError.EMAIL_HAS_BEEN_USED);
        }

        User user = new User(
                registerRequestModel.getEmail(),
                passwordEncoder.encode(registerRequestModel.getPassword())
        );

        user.addRole(
                roleRepository.findByName(registerRequestModel.getRole())
                        .orElseThrow(() ->
                                new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE))
        );

        return userRepository.saveAndFlush(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomIllegalArgumentException(ServerError.EMAIL_NOT_EXISTS));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.USER_ID_NOT_EXISTS
                ));
    }

    public User enableUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public boolean isAdmin(Long id) {
        User user = getUserById(id);
        return user
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(UserType.ADMIN));
    }

    public boolean isCandidate(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY));
    }

    public boolean isCompany(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY));
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
