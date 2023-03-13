package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.error.Error;
import com.uet.jobfinder.error.LoginError;
import com.uet.jobfinder.error.RegisterError;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.ConfirmValidationKeyModel;
import com.uet.jobfinder.model.LoginRequestModel;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.model.UserModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.repository.ValidationKeyRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private ModelMapper modelMapper;
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
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;

    public UserModel register(RegisterRequestModel registerRequestModel) {
        if (userRepository.findByEmail(registerRequestModel.getEmail()).isPresent()) {
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

        user = userRepository.save(user);
        userRepository.flush();

        //  TODO : Fix this when merge with cuong
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY))) {
            Company company = new Company();
            company.setUser(user);
            companyRepository.save(company);
        }

        sendEmailVerification(user.getEmail());

        UserModel userModel = new UserModel();
        modelMapper.map(user, userModel);
        return userModel;
    }

    public Boolean confirmRegister(ConfirmValidationKeyModel confirmValidationKeyModel) {
        //  Check if user with email is exists
        User user = userRepository.findByEmail(confirmValidationKeyModel.getEmail())
                .orElseThrow(() -> new CustomIllegalArgumentException(ServerError.EMAIL_NOT_EXISTS));

        //  Find validation key
        ValidationKey validationKey = validationKeyRepository.findByUserAndValidationKey(
                user, confirmValidationKeyModel.getConfirmationKey()
        ).orElseThrow(() ->
                new CustomIllegalArgumentException(ServerError.INCORRECT_VALIDATION_KEY));

        //  ValidationKey is used.
        if (validationKey.isActivated()) {
            throw new CustomIllegalArgumentException(ServerError.EXPIRED_VALIDATION_KEY);
        }

        //  ValidationKey is expired
        if (LocalDateTime.now().isAfter(validationKey.getExpirationDate())) {
            throw new CustomIllegalArgumentException(ServerError.EXPIRED_VALIDATION_KEY);
        }

        //  TODO: May be this line is not necessary
        if (LocalDateTime.now().isAfter(validationKey.getExpirationDate())) {
            return false;
        }

        validationKey.setActivated(true);
        validationKey.setExpirationDate(LocalDateTime.now());
        validationKeyRepository.save(validationKey);

        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public Boolean sendEmailVerification(String email) {
        User user = userService.getUserByEmail(email);
        ValidationKey validationKey = validationKeyService.createNewValidationKey(user);

        //  Prepare and send confirmation email
        StringBuilder emailContent = new StringBuilder();
        try {
            File file = new File("src/main/resources/templates/email_verification.html");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for (String line : bufferedReader.lines().collect(Collectors.toList())) {
                emailContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomIllegalArgumentException(ServerError.SERVER_ERROR);
        }
        emailService.sendEmail(user.getEmail(),
                String.format(emailContent.toString(),validationKey.getValidationKey().toString()));
        return true;
    }

    public Map<String, String> login(LoginRequestModel loginRequestModel) {
        User user = userService.getUserByEmail(loginRequestModel.getEmail());

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
