package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.Company;
import com.uet.jobfinder.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);

    //    @Query("SELECT * FROM Company c WHERE c.companyName = :companyName and c. = :name")
//    List<Company> findByTitleAndStar(String title, Byte start);
    @Query(
            value = "SELECT * FROM company WHERE MATCH(company_name) AGAINST(?1)",
            nativeQuery = true
    )
    Page<Company> searchAllCompany(Pageable pageable, String search);

    void deleteById(Long id);
}
