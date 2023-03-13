package com.uet.jobfinder.configuration;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.model.UserModel;
import org.modelmapper.*;
import org.modelmapper.builder.ConfigurableConditionExpression;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.MatchingStrategy;
import org.modelmapper.spi.SourceGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        configMapJobToJobModel(modelMapper);

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

    private TypeMap configMapJobToJobModel(ModelMapper modelMapper) {
        TypeMap<Job, JobModel> jobMapper = modelMapper.createTypeMap(Job.class, JobModel.class);
        jobMapper.addMappings(expression ->
                expression.map(Job::getId, JobModel::setId));
        jobMapper.addMappings(expression ->
                expression.map(job -> job.getCompany().getUser().getId(), JobModel::setUserId));
        return jobMapper;
    }

}
