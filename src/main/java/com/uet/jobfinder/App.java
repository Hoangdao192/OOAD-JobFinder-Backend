package com.uet.jobfinder;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.Job;
import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.model.JobModel;
import com.uet.jobfinder.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        ModelMapper modelMapper = context.getBean(ModelMapper.class);

        Job job = new Job();
        job.setCompany(Company.builder().id(123L).user(User.builder().id(123L).build()).build());
        JobModel jobModel = new JobModel();

        modelMapper.map(job, jobModel);
        System.out.println(jobModel.getUserId());
    }

}
