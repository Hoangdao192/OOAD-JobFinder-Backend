package com.uet.jobfinder.repository;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValidationKeyRepository extends JpaRepository<ValidationKey, Long> {
    Optional<ValidationKey> findByUserAndValidationKey(User user, Long validationKey);
    List<ValidationKey> findAllByUser(User user);
}
