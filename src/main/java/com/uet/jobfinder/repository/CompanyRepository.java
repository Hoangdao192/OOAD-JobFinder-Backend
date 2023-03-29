package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);

//    @Query("SELECT * FROM Company c WHERE c.companyName = :companyName and c. = :name")
//    List<Company> findByTitleAndStar(String title, Byte start);

    void deleteById(Long id);
}
