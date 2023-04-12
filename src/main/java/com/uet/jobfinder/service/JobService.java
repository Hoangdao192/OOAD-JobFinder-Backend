package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Address;
import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.JobStatus;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.model.AddressModel;
import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.model.PageQueryModel;
import com.uet.jobfinder.presentation.JobMonthStatisticPresentation;
import com.uet.jobfinder.presentation.JobYearStatisticPresentation;
import com.uet.jobfinder.repository.AddressRepository;
import com.uet.jobfinder.repository.CompanyRepository;
import com.uet.jobfinder.repository.JobRepository;
import com.uet.jobfinder.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;
    private CompanyService companyService;
    private UserService userService;
    private ModelMapper modelMapper;
    private JsonWebTokenProvider jsonWebTokenProvider;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AddressRepository addressRepository;

    public List<Object> getStatistic(
            Integer month, Integer year
    ) {
        if (month == 0) {
            return getStatisticByYear(year);
        }
        return getStatisticByMonth(month, year);
    }

    public List<Object> getStatisticByYear(Integer year) {
        List<JobYearStatisticPresentation> statistic = jobRepository.getYearStatistic(year);

        List<Object> jobStatistic = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            int mMonth = i;
            jobStatistic.add(new Object() {
                public Integer month = mMonth;
                public Long numberOfJob = 0L;
            });
        }

        for (JobYearStatisticPresentation statisticPresentation: statistic) {
            jobStatistic.set(statisticPresentation.getMonth() - 1, new Object() {
                public Integer month = statisticPresentation.getMonth();
                public Long numberOfJob = statisticPresentation.getNumberOfJob();
            });
        }

        return jobStatistic;
    }

    public List<Object> getStatisticByMonth(Integer month, Integer year) {
        List<JobMonthStatisticPresentation> statistic = jobRepository.getMonthStatistic(month, year);

        List<Object> jobStatistic = new ArrayList<>();
        for (int i = 1; i <= 31; ++i) {
            int mMonth = i;
            jobStatistic.add(new Object() {
                public Integer day = mMonth;
                public Long numberOfJob = 0L;
            });
        }

        for (JobMonthStatisticPresentation statisticPresentation: statistic) {
            jobStatistic.set(statisticPresentation.getDay() - 1, new Object() {
                public Integer day = statisticPresentation.getDay();
                public Long numberOfJob = statisticPresentation.getNumberOfJob();
            });
        }

        return jobStatistic;
    }

    public Long countOpenJobByCompanyId(
            Long companyId, HttpServletRequest request
    ) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(userId);

        if (!userId.equals(companyId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.ACCESS_DENIED
            );
        }

        return jobRepository.countJobByCompanyAndStatus(company, JobStatus.OPEN);
    }

    public JobModel createJob(JobModel jobModel, HttpServletRequest request) {
        Long userId = jsonWebTokenProvider.getUserIdFromRequest(request);
        Company company = companyService.getCompanyByUserId(userId);

        if (jobModel.getCloseDate().isBefore(LocalDate.now())) {
            throw new CustomIllegalArgumentException(
                    ServerError.INVALID_JOB_CLOSE_DATE
            );
        }

        Job job = Job.builder()
                .company(company)
                .jobTitle(jobModel.getJobTitle())
                .jobDescription(jobModel.getJobDescription())
                .major(jobModel.getMajor())
                .salary(jobModel.getSalary())
                .numberOfHiring(jobModel.getNumberOfHiring())
                .requireExperience(jobModel.getRequireExperience())
                .sex(jobModel.getSex())
                .workingForm(jobModel.getWorkingForm())
                .status(JobStatus.OPEN)
                .openDate(LocalDate.now())
                .closeDate(jobModel.getCloseDate())
                .build();

        if (jobModel.getJobAddress() != null) {
            System.out.println("CREATE ADDRESS");
            AddressModel addressModel = jobModel.getJobAddress();
            Address address = Address.builder()
                    .province(addressModel.getProvince())
                    .district(addressModel.getDistrict())
                    .ward(addressModel.getWard())
                    .detailAddress(addressModel.getDetailAddress())
                    .build();
            address = addressRepository.save(address);
            job.setJobAddress(address);
        } else if (company.getAddress() != null) {
            System.out.println("COMPANY ADDRESS");
            job.setJobAddress(company.getAddress());
        }

        Job newJob = jobRepository.save(job);

        modelMapper.map(newJob, jobModel);
        return jobModel;
    }

    public PageQueryModel<JobModel> getAllJob(
            Integer page, Integer perPage,
            Long companyId, String jobTitle,
            String major, String workingForm, Boolean isJobOpen
    ) {
        Page<Job> jobPage = null;
        if (jobTitle == null || jobTitle.length() == 0) {
            jobPage = jobRepository.findAllWithOutTitle(
                    PageRequest.of(page, perPage),
                    companyId, major, workingForm
            );
        } else {
            jobPage = jobRepository.findAllWithJobTitle(
                    PageRequest.of(page, perPage),
                    companyId, jobTitle, major, workingForm
            );
        }

        return new PageQueryModel<>(
                new PageQueryModel.PageModel(
                        jobPage.getPageable() != null ? jobPage.getPageable().getPageNumber() : 0,
                        jobPage.getPageable() != null ? jobPage.getPageable().getPageSize() : 0,
                        jobPage.getTotalPages()
                ),
                jobPage.getContent().stream()
                        .filter((job) -> {
                            if (isJobOpen != null) {
                                return job.getCloseDate().isAfter(LocalDate.now()) == isJobOpen;
                            }
                            return true;
                        })
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
        if (jobModel.getJobAddress() != null) {
            AddressModel addressModel = jobModel.getJobAddress();
            job.setJobAddress(Address.builder()
                    .province(addressModel.getProvince())
                    .district(addressModel.getDistrict())
                    .ward(addressModel.getWard())
                    .detailAddress(addressModel.getDetailAddress())
                    .build());
        }
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
        if (!job.getCompany().getId().equals(company.getId())
                && !userService.isAdmin(userId)) {
            throw new CustomIllegalArgumentException(
                    ServerError.COMPANY_NOT_OWN_JOB
            );
        }

        company.getJobList().remove(job);
        companyRepository.save(company);
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

    public Long countAllJob() {
        return jobRepository.count();
    }
}
