package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.presentation.UserMonthStatisticPresentation;
import com.uet.jobfinder.presentation.UserYearStatisticPresentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT count(*) as numberOfUser, MONTH(user.create_date) as month, role.name as role FROM jobfinder.user " +
            "inner join user_role on user_role.user_id = user.id " +
            "inner join role on role.id = user_role.role_id " +
            "where role.name != 'Admin' and YEAR(user.create_date) = :year " +
            "group by MONTH(user.create_date), role.name;", nativeQuery = true)
    List<UserYearStatisticPresentation> getUserYearGrowthStatistic(@Param("year") Integer year);

    @Query(value = "SELECT count(*) as numberOfUser, DAY(user.create_date) as day, role.name as role FROM jobfinder.user " +
            "inner join user_role on user_role.user_id = user.id " +
            "inner join role on role.id = user_role.role_id " +
            "where role.name != 'Admin' and MONTH(user.create_date) = :month and YEAR(user.create_date) = :year " +
            "group by DAY(user.create_date), role.name;", nativeQuery = true)
    List<UserMonthStatisticPresentation> getUserMonthGrowthStatistic(
            @Param("month") Integer month, @Param("year") Integer year);

    @Query(nativeQuery = true,
        value = "SELECT count(*) FROM jobfinder.user " +
            "inner join user_role on user_role.user_id = user.id " +
            "inner join role on role.id = user_role.role_id " +
            "where role.name = :role")
    Long countAllByRole(String role);

    @Query(value = "select user.* from user " +
            "join user_role on user_role.user_id = user.id " +
            "where (:email is null or email like %:email%) " +
            "and (:roleId is null or role_id = :roleId) " +
            "and (:isActive is null or enabled = :isActive) " +
            "and (:isLocked is null or locked = :isLocked)",
            nativeQuery = true)
    Page<User> searchUser(
            Pageable pageable,
            @Param("email") String email, @Param("roleId") Long roleId,
            @Param("isActive") Boolean isActive, @Param("isLocked") Boolean isLocked);
}
