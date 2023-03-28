package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.*;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.JobApplicationModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.repository.JobApplicationRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;
    private CandidateService candidateService;
    private JobService jobService;
    private FileService fileService;
    private ModelMapper modelMapper;
    private JsonWebTokenProvider jsonWebTokenProvider;
    private CompanyService companyService;
    private UserService userService;

    public JobApplicationModel createJobApplication(
            JobApplicationModel jobApplicationModel, HttpServletRequest request) throws IOException {

        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Candidate candidate = candidateService
                .getCandidateById(jobApplicationModel.getCandidateId());
        Job job = jobService.getJobById(jobApplicationModel.getJobId());

        if (job.getCloseDate().isBefore(LocalDate.now())) {
            throw new CustomIllegalArgumentException(
                    ServerError.JOB_CLOSED
            );
        }

        //  Because client can send any candidateId in jobApplicationModel
        //  So we need to check if this candidate id is jobApplicationModel.candidateId or not.
        if (!candidate.getUser().getId().equals(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        if (getJobApplicationByCandidateAndJob(
                candidate, job
        ) != null) {
            throw new CustomIllegalArgumentException(
                    ServerError.CANDIDATE_ALREADY_APPLIED
            );
        }

        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .status(JobApplicationStatus.WAITING)
                .appliedDate(LocalDateTime.now())
                .build();

        if (jobApplicationModel.getCvFile() != null) {
            MultipartFile cvFile = jobApplicationModel.getCvFile();
            jobApplication.setCvFile(
                    fileService.saveFile(
                            cvFile.getOriginalFilename(),
                            cvFile.getContentType(),
                            cvFile.getBytes()
                    )
            );
        }

        return modelMapper.map(
                jobApplicationRepository.save(jobApplication),
                JobApplicationModel.class);
    }

    public boolean acceptJobApplication(
            Long jobApplicationId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(userId);

        JobApplication jobApplication = getJobApplicationById(jobApplicationId);
        if (!jobApplication.getJob().getCompany().getId().equals(
                company.getId()
        )) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        jobApplication.setStatus(JobApplicationStatus.ACCEPTED);
        jobApplication.setUpdatedDate(LocalDateTime.now());
        jobApplicationRepository.save(jobApplication);
        return true;
    }

    public boolean rejectJobApplication(
            Long jobApplicationId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(userId);

        JobApplication jobApplication = getJobApplicationById(jobApplicationId);
        if (!jobApplication.getJob().getCompany().getId().equals(
                company.getId()
        )) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        jobApplication.setStatus(JobApplicationStatus.REJECTED);
        jobApplication.setUpdatedDate(LocalDateTime.now());
        jobApplicationRepository.save(jobApplication);
        return true;
    }

    public JobApplicationModel getJobApplicationModelById(
            Long jobApplicationId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        JobApplication jobApplication = getJobApplicationById(jobApplicationId);
        if (!jobApplication.getJob().getCompany().getUser().getId().equals(userId)
            && !jobApplication.getCandidate().getUser().getId().equals(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return modelMapper.map(jobApplication, JobApplicationModel.class);
    }

    public JobApplication getJobApplicationByCandidateAndJob(Candidate candidate, Job job) {
        return jobApplicationRepository.findByJobAndCandidate(
                job, candidate
        ).orElse(null);
    }

    public JobApplication getJobApplicationById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(
                        ServerError.JOB_APPLICATION_NOT_EXISTS
                ));
    }

    public PageQueryModel<JobApplicationModel> listJobApplication(
            Integer page, Integer pageSize, Long candidateId, Long jobId,
            HttpServletRequest request
    ) {
        if (candidateId == -1 && jobId == -1) {
            return new PageQueryModel<>(
                    new PageQueryModel.PageModel(
                            0, 0, 0
                    ),
                    new ArrayList<>()
            );
        }

        //  Company get all application applied the job
        if (candidateId == -1) {
            return getAllJobApplicationByJobId(
                    page, pageSize, jobId, request);
        }

        //  Candidate get all application had applied
        if (jobId == -1) {
            return getAllJobApplicationByCandidateId(
                    page, pageSize, candidateId, request);
        }

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        0, 0, 0
                ),
                new ArrayList<>()
        );
    }

    /**
     *  This is call when company want to get all application that applied this job
      */
    public PageQueryModel<JobApplicationModel> getAllJobApplicationByJobId(
            Integer page, Integer pageSize, Long jobId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Job job = jobService.getJobById(jobId);

        //  Only company own this job and admin can do this
        if (!job.getCompany().getUser().getId().equals(userId)
            && !userService.isAdmin(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Page<JobApplication> jobApplications = jobApplicationRepository.findAllByJob(
                PageRequest.of(page, pageSize), job
        );

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        jobApplications.getPageable().getPageNumber(),
                        jobApplications.getPageable().getPageSize(),
                        jobApplications.getTotalPages()
                ),
                jobApplications.getContent().stream().map(model ->
                        modelMapper.map(model, JobApplicationModel.class))
                        .collect(Collectors.toList())
        );
    }

    public boolean deleteJobApplication(Long jobApplicationId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Candidate candidate = candidateService.getCandidateById(userId);

        JobApplication jobApplication = getJobApplicationById(jobApplicationId);

        if (jobApplication.getCandidate().getId() != userId) {
            throw new CustomIllegalArgumentException(
                ServerError.ACCESS_DENIED
            );
        }

        candidate.getJobApplications().remove(jobApplication);
        jobApplicationRepository.delete(jobApplication);
        return true;
    }

    public PageQueryModel<JobApplicationModel> getAllJobApplicationByCandidateId(
            Integer page, Integer pageSize, Long candidateId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Candidate candidate = candidateService.getCandidateById(candidateId);

        //  Only candidate and admin can do this
        if (!candidateId.equals(userId) && !userService.isAdmin(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        Page<JobApplication> jobApplications = jobApplicationRepository.findAllByCandidate(
                PageRequest.of(page, pageSize), candidate
        );

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        jobApplications.getPageable().getPageNumber(),
                        jobApplications.getPageable().getPageSize(),
                        jobApplications.getTotalPages()
                ),
                jobApplications.getContent().stream().map(model ->
                                modelMapper.map(model, JobApplicationModel.class))
                        .collect(Collectors.toList())
        );
    }

    @Autowired
    public void setJsonWebTokenProvider(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    @Autowired
    public void setJobApplicationRepository(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Autowired
    public void setCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
