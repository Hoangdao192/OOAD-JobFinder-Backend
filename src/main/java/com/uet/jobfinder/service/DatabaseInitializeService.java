package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.Role;
import com.uet.jobfinder.entity.UserType;
import com.uet.jobfinder.repository.JobRepository;
import com.uet.jobfinder.repository.RoleRepository;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
@Transactional
public class DatabaseInitializeService {

    private EntityManager entityManager;

    public DatabaseInitializeService() {

    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
//        if (roleRepository.findByName(UserType.ADMIN).isEmpty()) {
//            roleRepository.save(new Role(UserType.ADMIN));
//        }
//        if (roleRepository.findByName(UserType.CANDIDATE).isEmpty()) {
//            roleRepository.save(new Role(UserType.CANDIDATE));
//        }
//        if (roleRepository.findByName(UserType.COMPANY).isEmpty()) {
//            roleRepository.save(new Role(UserType.COMPANY));
//        }
    }

    @Autowired
    @Transactional
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
//        EntityManager em = applicationContext.getBean(EntityManager.class);

//        Query query = entityManager.createNativeQuery("alter table job add fulltext (job_title)");
////        entityManager.joinTransaction();
//        query.executeUpdate();
//        System.out.println(i);
//        entityManager.close();
//        emf.close();
    }
}
