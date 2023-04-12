package com.uet.jobfinder.configuration;

import com.uet.jobfinder.entity.*;
import com.uet.jobfinder.model.*;
import com.uet.jobfinder.service.FileService;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfiguration {

    private FileService fileService;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        configMapJobToJobModel(modelMapper);
        configMapCompanyToCompanyModel(modelMapper);
        configMapCandidateToCandidateModel(modelMapper);
        configMapJobApplicationToJobApplicationModel(modelMapper);
        configMapSavedJobToSaveJobModel(modelMapper);

        TypeMap<User, UserModel> userMapper = modelMapper.createTypeMap(User.class, UserModel.class);
        Converter<Set<Role>, List<String>> roleConverter = mappingContext -> mappingContext
                .getSource()
                .stream()
                .map(Role::getName).collect(Collectors.toList());
        userMapper.addMappings(mapper ->
                mapper
                        .using(roleConverter)
                        .map(User::getRoles, UserModel::setRoles));
        return modelMapper;
    }

    private void configMapJobToJobModel(ModelMapper modelMapper) {
        TypeMap<Job, JobModel> jobMapper = modelMapper.createTypeMap(Job.class, JobModel.class);
        jobMapper.addMappings(expression ->
                expression.map(Job::getId, JobModel::setId));
        jobMapper.addMappings(expression ->
                expression.map(job -> job.getCompany().getUser().getId(), JobModel::setUserId));
        Converter<Company, CompanyModel> converter = mappingContext -> {
            if (mappingContext.getSource() != null) {
                return modelMapper.map(mappingContext.getSource(), CompanyModel.class);
            }
            return null;
        };
        jobMapper.addMappings(expression ->
                expression
                    .using(converter)
                    .map(Job::getCompany, JobModel::setCompany));
    }

    private void configMapCompanyToCompanyModel(ModelMapper modelMapper) {
        TypeMap<Company, CompanyModel> companyMapper = modelMapper.createTypeMap(
                Company.class, CompanyModel.class
        );
        companyMapper.addMappings(mapper ->
                mapper.using(userToLongConverter())
                        .map(Company::getUser, CompanyModel::setUserId));

        Converter<AppFile, String> fileConverter = mappingContext -> {
            if (mappingContext.getSource() != null) {
                return fileService.generateFileUrl(mappingContext.getSource().getId());
            }
            return null;
        };
        companyMapper.addMappings(mapper ->
                mapper.using(fileConverter)
                        .map(Company::getCompanyLogo, CompanyModel::setCompanyLogo));
    }

    private void configMapCandidateToCandidateModel(ModelMapper modelMapper) {
        TypeMap<Candidate, CandidateModel> candidateMapper = modelMapper.createTypeMap(
                Candidate.class, CandidateModel.class
        );

        candidateMapper.addMappings(mapper ->
                mapper.using(userToLongConverter())
                        .map(Candidate::getUser, CandidateModel::setUserId));

        candidateMapper.addMappings(mapper -> 
                mapper.using(appFileToUrlConverter())
                    .map(Candidate::getAvatar, CandidateModel::setAvatar));
    }

    private void configMapJobApplicationToJobApplicationModel(ModelMapper modelMapper) {
        TypeMap<JobApplication, JobApplicationModel> jobApplicationMapper =
                modelMapper.createTypeMap(JobApplication.class, JobApplicationModel.class);

        jobApplicationMapper.addMappings(mapper ->
                mapper.using(jobToLongConverter())
                        .map(JobApplication::getJob, JobApplicationModel::setJobId));

        jobApplicationMapper.addMappings(mapper ->
                mapper.using(appFileToUrlConverter())
                        .map(JobApplication::getCvFile, JobApplicationModel::setCv));
    }

    private void configMapSavedJobToSaveJobModel(ModelMapper modelMapper) {
        TypeMap<SavedJob, SavedJobModel> savedJobMapper =
                modelMapper.createTypeMap(SavedJob.class, SavedJobModel.class);

        savedJobMapper.addMappings(mapper ->
                mapper.using(candidateToLongConverter())
                        .map(SavedJob::getCandidate, SavedJobModel::setCandidateId));
    }

    private Converter<User, Long> userToLongConverter() {
        return mappingContext -> {
            if (mappingContext.getSource() != null) {
                return mappingContext.getSource().getId();
            }
            return null;
        };
    }

    private Converter<AppFile, String> appFileToUrlConverter() {
        return mappingContext -> {
            if (mappingContext.getSource() != null) {
                return fileService.generateFileUrl(mappingContext.getSource().getId());
            }
            return null;
        };
    }

    private Converter<Candidate, Long> candidateToLongConverter() {
        return mappingContext -> {
            if (mappingContext.getSource() != null) {
                return mappingContext.getSource().getId();
            }
            return null;
        };
    }

    private Converter<Job, Long> jobToLongConverter() {
        return mappingContext -> {
            if (mappingContext.getSource() != null) {
                return mappingContext.getSource().getId();
            }
            return null;
        };
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
