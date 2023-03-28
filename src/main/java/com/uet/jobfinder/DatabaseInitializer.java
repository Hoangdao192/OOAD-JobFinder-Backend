package com.uet.jobfinder;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.UserType;
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
//        createTestCandidate();
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
        RegisterRequestModel companyRequestModel = new RegisterRequestModel();
        companyRequestModel.setEmail("20020376@vnu.edu.vn");
        companyRequestModel.setPassword(passwordEncoder.encode("12345678"));
        companyRequestModel.setRole(UserType.CANDIDATE);
        User user = userService.createUser(
                companyRequestModel
        );

        if (userService.isCandidate(user)) {
            candidateService.createEmptyCandidate(user);
        }
    }

    private void createTestCompany() {
        RegisterRequestModel companyRequestModel = new RegisterRequestModel();
        companyRequestModel.setEmail("20020390@vnu.edu.vn");
        companyRequestModel.setPassword("12345678");
        companyRequestModel.setRole(UserType.COMPANY);
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
                        .build()
        );
    }
}
