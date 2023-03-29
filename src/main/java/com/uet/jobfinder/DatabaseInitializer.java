package com.uet.jobfinder;

import com.uet.jobfinder.entity.*;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import com.uet.jobfinder.service.CandidateService;
import com.uet.jobfinder.service.CompanyService;
import com.uet.jobfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUserRole();
        createTestCompany();
        createTestCandidate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
//        createTestCompany();
    }

    private void createUserRole() {
        if (roleRepository.findByName(UserType.ADMIN).isEmpty()) {
            roleRepository.save(new Role(UserType.ADMIN));
        }
        if (roleRepository.findByName(UserType.CANDIDATE).isEmpty()) {
            roleRepository.save(new Role(UserType.CANDIDATE));
        }
        if (roleRepository.findByName(UserType.COMPANY).isEmpty()) {
            roleRepository.save(new Role(UserType.COMPANY));
        }
        roleRepository.flush();
    }

    private void createTestCandidate() {
        User user = new User("20020376@vnu.edu.vn", passwordEncoder.encode("12345678"));
        user.addRole(roleRepository.findByName(UserType.CANDIDATE)
                .orElseThrow(() ->
                        new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE)));
        user.setEnabled(true);

        candidateService.createCandidate(
                Candidate.builder().user(user)
                        .fullName("Hoàng Đạo")
                        .sex("Male")
                        .dateOfBirth(LocalDate.of(2002, 2, 19))
                        .contactEmail("20020376@vnu.edu.vn")
                        .selfDescription("Sinh viên CNTT")
                        .phoneNumber("0325135251")
                        .build()
        );
    }

    private void createTestCompany() {
        User user = new User("20020390@vnu.edu.vn", passwordEncoder.encode("12345678"));
        user.addRole(roleRepository.findByName(UserType.COMPANY)
                .orElseThrow(() ->
                        new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE)));
        user.setEnabled(true);

        //  Create a company
        companyService.createCompany(
                Company.builder().user(user)
                        .companyDescription("A good company")
                        .companyName("UET Software")
                        .numberOfEmployee("1000+")
                        .address(
                                Address.builder()
                                        .province("Hà Nội")
                                        .district("Cầu Giấy")
                                        .ward("Xuân Thủy")
                                        .detailAddress("144").build()
                        )
                        .build()
        );
    }
}
