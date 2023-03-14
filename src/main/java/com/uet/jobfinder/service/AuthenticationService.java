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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
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

    public UserModel register(RegisterRequestModel registerRequestModel) {
        User user = userService.createUser(registerRequestModel);

        //  Create a company
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(UserType.COMPANY))) {
            companyService.createEmptyCompany(user);
        }

        sendEmailVerification(user.getEmail());

        UserModel userModel = new UserModel();
        modelMapper.map(user, userModel);
        return userModel;
    }

    public Boolean confirmRegister(ConfirmValidationKeyModel confirmValidationKeyModel) {
        User user = userService.getUserByEmail(confirmValidationKeyModel.getEmail());

        //  Find validation key
        ValidationKey validationKey = validationKeyService.findByUserAndValidationKey(
                user, confirmValidationKeyModel.getConfirmationKey().toString()
        );

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

        validationKeyService.activeValidationKey(validationKey);

        userService.enableUser(user);
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

    public JsonWebTokenModel login(LoginRequestModel loginRequestModel) {
        User user = userService.getUserByEmail(loginRequestModel.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequestModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Return JWT
        String jwt = jwtProvider.generateToken((User) authentication.getPrincipal());
        return new JsonWebTokenModel("Bearer", jwt);
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
