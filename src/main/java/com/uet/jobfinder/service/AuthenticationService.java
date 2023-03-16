package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.*;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;
    private JsonWebTokenProvider jwtProvider;
    private ValidationKeyService validationKeyService;
    private EmailService emailService;
    private UserService userService;
    private CompanyService companyService;
    @Autowired
    private CandidateService candidateService;

    public UserModel register(RegisterRequestModel registerRequestModel) {
        User user = userService.createUser(registerRequestModel);

        //  Create a company
        if (userService.isCompany(user)) {
            companyService.createEmptyCompany(user);
        }
        //  Create a candidate
        else if (userService.isCandidate(user)) {
            candidateService.createEmptyCandidate(user);
        }

        sendEmailVerification(user.getEmail());

        UserModel userModel = new UserModel();
        modelMapper.map(user, userModel);
        return userModel;
    }

    public Map<String, Object> confirmRegister(ConfirmValidationKeyModel confirmValidationKeyModel) {
        User user = userService.getUserByEmail(confirmValidationKeyModel.getEmail());

        //  Find validation key
        ValidationKey validationKey = validationKeyService.findByUserAndValidationKey(
                user, confirmValidationKeyModel.getConfirmationKey()
        );

        if (validationKeyService.isValidationKeyExpired(validationKey)) {
            throw new CustomIllegalArgumentException(
                    ServerError.EXPIRED_VALIDATION_KEY
            );
        }

        validationKeyService.activeValidationKey(validationKey);

        userService.enableUser(user);

        //  Return JWT
        String jwt = jwtProvider.generateToken(user);
        return Map.of(
                "tokenType", "Bearer",
                "accessToken", jwt,
                "user", modelMapper.map(user, UserModel.class)
        );
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
                String.format(emailContent.toString(),validationKey.getValidationKey()));
        return true;
    }

    public Map<String, Object> login(LoginRequestModel loginRequestModel) {
        User user = userService.getUserByEmail(loginRequestModel.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequestModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Return JWT
        String jwt = jwtProvider.generateToken((User) authentication.getPrincipal());
        return Map.of(
                "tokenType", "Bearer",
                "accessToken", jwt,
                "user", modelMapper.map(user, UserModel.class)
        );
//        return new JsonWebTokenModel("Bearer", jwt);
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
    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
