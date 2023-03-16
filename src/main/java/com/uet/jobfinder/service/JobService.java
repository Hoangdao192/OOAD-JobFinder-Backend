package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.repository.JobRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;
    private CompanyService companyService;
    private UserService userService;
    private ModelMapper modelMapper;
    private JsonWebTokenProvider jsonWebTokenProvider;

    public JobModel createJob(JobModel jobModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(userId);

        Job job = Job.builder()
                .company(company)
                .jobTitle(jobModel.getJobTitle())
                .jobDescription(jobModel.getJobDescription())
                .jobAddress(jobModel.getJobAddress())
                .major(jobModel.getMajor())
                .salary(jobModel.getSalary())
                .numberOfHiring(jobModel.getNumberOfHiring())
                .requireExperience(jobModel.getRequireExperience())
                .sex(jobModel.getSex())
                .workingForm(jobModel.getWorkingForm())
                .build();
        Job newJob = jobRepository.save(job);

        modelMapper.map(newJob, jobModel);
        return jobModel;
    }

    public PageQueryModel<JobModel> getAllJob(Long page, Long perPage) {
        Page<Job> jobPage = jobRepository.findAll(
                PageRequest.of(page.intValue(), perPage.intValue())
        );

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        jobPage.getPageable().getPageNumber(),
                        jobPage.getPageable().getPageSize(),
                        jobPage.getTotalPages()
                ),
                jobPage.getContent().stream()
                        .map(job -> {
                            JobModel jobModel = new JobModel();
                            modelMapper.map(job, jobModel);
                            return jobModel;
                        }).collect(Collectors.toList())
        );
    }

    public JobModel updateJob(JobModel jobModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Job job = getJobById(jobModel.getId());
        Company company = companyService.getCompanyByUserId(userId);

        //  Check if this company own this job, so they can update it information
        if (!job.getCompany().getId().equals(company.getId())
                && !userService.isAdmin(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.COMPANY_NOT_OWN_JOB
            );
        }

        //  Update job information
        job.setJobTitle(jobModel.getJobTitle());
        job.setJobDescription(jobModel.getJobDescription());
        job.setJobAddress(jobModel.getJobAddress());
        job.setMajor(jobModel.getMajor());
        job.setSalary(jobModel.getSalary());
        job.setNumberOfHiring(jobModel.getNumberOfHiring());
        job.setRequireExperience(jobModel.getRequireExperience());
        job.setSex(jobModel.getSex());
        job.setWorkingForm(jobModel.getWorkingForm());

        job = jobRepository.save(job);

        modelMapper.map(job, jobModel);
        return jobModel;
    }

    public boolean deleteJob(Long jobId, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);

        Job job = getJobById(jobId);
        Company company = companyService.getCompanyByUserId(userId);

        //  Only admin and company whose own the job post can delete the job
        if (job.getCompany().getId().equals(company.getId())
                && !userService.isAdmin(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.COMPANY_NOT_OWN_JOB
            );
        }

        jobRepository.delete(job);
        return true;
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(ServerError.JOB_NOT_EXISTS));
    }

    public JobModel getJobModelById(Long id) {
        Job job = getJobById(id);
        JobModel jobModel = new JobModel();
        modelMapper.map(job, jobModel);
        return jobModel;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setJsonWebTokenProvider(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }
}