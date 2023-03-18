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

    @Autowired
    private FileService fileService;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        configMapJobToJobModel(modelMapper);
        configMapCompanyToCompanyModel(modelMapper);
        configMapCandidateToCandidateModel(modelMapper);

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
    }

    private void configMapCompanyToCompanyModel(ModelMapper modelMapper) {
        TypeMap<Company, CompanyModel> companyMapper = modelMapper.createTypeMap(
                Company.class, CompanyModel.class
        );
        Converter<User, Long> userConverter = mappingContext ->
                mappingContext.getSource().getId();
        companyMapper.addMappings(mapper ->
                mapper.using(userConverter)
                        .map(Company::getUser, CompanyModel::setUserId));

        Converter<Address, AddressModel> addressConverter = mappingContext -> {
            if (mappingContext.getSource() != null) {
                return modelMapper.map(mappingContext.getSource(), AddressModel.class);
            }
            return null;
        };
        companyMapper.addMappings(mapper ->
                mapper.using(addressConverter)
                        .map(Company::getAddress, CompanyModel::setAddress));

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
        Converter<Address, AddressModel> addressConverter = mappingContext -> {
            if (mappingContext.getSource() != null) {
                return modelMapper.map(mappingContext.getSource(), AddressModel.class);
            }
            return null;
        };
        candidateMapper.addMappings(mapper ->
                mapper.using(addressConverter)
                        .map(Candidate::getAddress, CandidateModel::setAddress));

        Converter<AppFile, String> fileConverter = mappingContext -> {
            if (mappingContext.getSource() != null) {
                return fileService.generateFileUrl(mappingContext.getSource().getId());
            }
            return null;
        };
        candidateMapper.addMappings(mapper -> 
                mapper.using(fileConverter)
                    .map(Candidate::getAvatar, CandidateModel::setAvatar));
    }

}
