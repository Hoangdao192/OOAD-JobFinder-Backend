package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.repository.ValidationKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ValidationKeyService {

    //  Second
    private Long expirationDuration = 120L;

    @Autowired
    private ValidationKeyRepository validationKeyRepository;

    public ValidationKey createNewValidationKey(User user) {
        Long randomKey = ((Double) (Math.random() * 1000000)).longValue();
        ValidationKey validationKey = new ValidationKey(
                randomKey, LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(expirationDuration),
                user
        );
        return validationKeyRepository.save(validationKey);
    }

}
