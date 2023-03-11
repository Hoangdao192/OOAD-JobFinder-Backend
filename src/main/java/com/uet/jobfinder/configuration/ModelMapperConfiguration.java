package com.uet.jobfinder.configuration;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.model.JobModel;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableConditionExpression;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.MatchingStrategy;
import org.modelmapper.spi.SourceGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<Job, JobModel> jobMapper = modelMapper.createTypeMap(Job.class, JobModel.class);
        jobMapper.addMappings(expression ->
                expression.map(Job::getId, JobModel::setId));
        jobMapper.addMappings(new ExpressionMap<Job, JobModel>() {
            @Override
            public void configure(ConfigurableConditionExpression<Job, JobModel> expression) {
                expression.map(job -> job.getCompany().getUser().getId(), JobModel::setUserId);
            }
        });

        return modelMapper;
    }

}
