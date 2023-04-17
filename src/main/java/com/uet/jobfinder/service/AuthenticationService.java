package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Candidate;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.dto.*;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserDTO register(RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);

        //  Create a company
        if (userService.isCompany(user)) {
            companyService.createEmptyCompany(user);
        }
        //  Create a candidate
        else if (userService.isCandidate(user)) {
            candidateService.createEmptyCandidate(user);
        }

        sendEmailVerification(user.getEmail());

        UserDTO userDTO = new UserDTO();
        modelMapper.map(user, userDTO);
        return userDTO;
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
                "user", modelMapper.map(user, UserDTO.class)
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
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomIllegalArgumentException(ServerError.SERVER_ERROR);
        }
        emailService.sendEmail(user.getEmail(),
                String.format(emailContent.toString(),validationKey.getValidationKey()));
        return true;
    }

    public UserLoginResponse login(LoginRequest loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Return JWT
        String jwt = jwtProvider.generateToken((User) authentication.getPrincipal());

        if (userService.isCandidate(user)) {
            Candidate candidate = candidateService.getCandidateByUser(user);
            return new CandidateLoginResponse(
                    modelMapper.map(user, UserDTO.class),
                    new JsonWebTokenDTO("Bearer", jwt),
                    modelMapper.map(candidate, CandidateDTO.class)
            );
        } else if (userService.isCompany(user)) {
            Company company = companyService.getCompanyByUser(user);
            return new CompanyLoginResponse(
                    modelMapper.map(user, UserDTO.class),
                    new JsonWebTokenDTO("Bearer", jwt),
                    modelMapper.map(company, CompanyDTO.class)
            );
        } else if (userService.isAdmin(user)) {
            return new UserLoginResponse(
                modelMapper.map(user, UserDTO.class),
                new JsonWebTokenDTO("Bearer", jwt)
            );
        }

        throw new CustomIllegalArgumentException(
                ServerError.INVALID_ROLE
        );
    }

    public boolean changePassword(
            ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Long userId = jwtProvider.getUserIdFromRequest(request);
        User user = userService.getUserById(userId);

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword, user.getPassword())) {
            throw new CustomIllegalArgumentException(
                    ServerError.WRONG_OLD_PASSWORD
            );
        }

        user.setPassword(
                passwordEncoder.encode(changePasswordRequest.newPassword)
        );
        userRepository.save(user);
        return true;
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
