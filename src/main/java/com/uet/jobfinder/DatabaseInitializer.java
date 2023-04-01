package com.uet.jobfinder;

import com.uet.jobfinder.entity.*;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.MajorService;
import com.uet.jobfinder.model.RegisterRequestModel;
import com.uet.jobfinder.repository.*;
import com.uet.jobfinder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private MajorService majorService;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobApplicationService jobApplicationService;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        createUserRole();
//        createMajor();
//
//        Company company = createTestCompany();
//        Candidate candidate = createTestCandidate();
//        List<Job> jobs = createJob(company);
//        createJobApplication(jobs, candidate);
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

    private Candidate createTestCandidate() {
        User user = new User("20020376@vnu.edu.vn", passwordEncoder.encode("12345678"));
        user.addRole(roleRepository.findByName(UserType.CANDIDATE)
                .orElseThrow(() ->
                        new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE)));
        user.setEnabled(true);

        return candidateService.createCandidate(
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

    private Company createTestCompany() {
        User user = new User("20020390@vnu.edu.vn", passwordEncoder.encode("12345678"));
        user.addRole(roleRepository.findByName(UserType.COMPANY)
                .orElseThrow(() ->
                        new CustomIllegalArgumentException(ServerError.INVALID_USER_ROLE)));
        user.setEnabled(true);

        //  Create a company
        return companyService.createCompany(
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

    private void createMajor() {
        majorService.createMajor(new Major("Công nghệ thông tin"));
        majorService.createMajor(new Major("Hệ thống thông tin"));
        majorService.createMajor(new Major("An toàn thông tin"));
        majorService.createMajor(new Major("Quản trị kinh doanh"));
        majorService.createMajor(new Major("Mạng máy tính và truyền thông dữ liệu"));
        majorService.createMajor(new Major("Logistics và Quản lý chuỗi cung ứng"));
        majorService.createMajor(new Major("Kỹ thuật máy tính"));
        majorService.createMajor(new Major("Công nghệ truyền thông"));
    }

    private List<Job> createJob(Company company) {
        Job firstJob = jobRepository.save(Job.builder()
                .major("Công nghệ thông tin")
                .sex("Male")
                .jobTitle("Lập tình viên Android")
                .jobDescription("Tuyển lập trình viên Android lương cao")
                .workingForm("Fulltime")
                .company(company)
                .numberOfHiring(10)
                .salary("5 triệu")
                .openDate(LocalDate.now())
                .status(JobStatus.OPEN)
                .requireExperience("5")
                .closeDate(LocalDate.of(2024,3,30))
                .build());

        Job secondJob = jobRepository.save(Job.builder()
                .major("Công nghệ thông tin")
                .sex("Male")
                .jobTitle("Lập tình viên Java")
                .jobDescription("Tuyển lập trình viên Java lương cao")
                .workingForm("Fulltime")
                .company(company)
                .numberOfHiring(10)
                .salary("15 triệu")
                .openDate(LocalDate.now())
                .status(JobStatus.OPEN)
                .requireExperience("1")
                .closeDate(LocalDate.of(2024,3,30))
                .build());

        Job thirdJob = jobRepository.save(Job.builder()
                .major("Công nghệ thông tin")
                .sex("Male")
                .jobTitle("Lập tình viên PhP")
                .jobDescription("Tuyển lập trình viên Java lương cao")
                .workingForm("Fulltime")
                .company(company)
                .numberOfHiring(10)
                .salary("105 triệu")
                .openDate(LocalDate.now())
                .status(JobStatus.OPEN)
                .requireExperience("1")
                .closeDate(LocalDate.of(2024,3,30))
                .build());

        return List.of(firstJob, secondJob, thirdJob);
    }

    private List<JobApplication> createJobApplication(List<Job> jobs, Candidate candidate) {
        for (Job job : jobs) {
            jobApplicationRepository.save(
                    JobApplication.builder()
                            .job(job)
                            .candidate(candidate)
                            .status(JobApplicationStatus.WAITING)
                            .appliedDate(LocalDateTime.now())
                            .build()
            )   ;
        }
        return null;
    }
}
