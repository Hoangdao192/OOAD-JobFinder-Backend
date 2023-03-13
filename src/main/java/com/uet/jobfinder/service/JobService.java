package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.repository.JobRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import com.uet.jobfinder.util.JsonWebTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JsonWebTokenProvider jsonWebTokenProvider;

    private JsonWebTokenUtil jsonWebTokenUtil = new JsonWebTokenUtil();

    public JobModel createJob(JobModel jobModel) {
        Company company = companyService.getCompanyByUserId(jobModel.getUserId());

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

    public JobModel updateJob(JobModel jobModel) {
        Job job = getJobById(jobModel.getId());
        User user = userService.getUserById(jobModel.getUserId());

        //  Check if this company own this job so they can update it information
        if (job.getCompany().getUser().getId() != user.getId()) {
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
        String jwtToken = jsonWebTokenUtil.getJWTFromRequest(request);
        Long userId = Long.parseLong(jsonWebTokenProvider.getUserIdFromJWT(jwtToken));

        Job job = getJobById(jobId);
        Company company = companyService.getCompanyByUserId(userId);

        if (job.getCompany().getUser().getId() != company.getUser().getId()) {
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

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
