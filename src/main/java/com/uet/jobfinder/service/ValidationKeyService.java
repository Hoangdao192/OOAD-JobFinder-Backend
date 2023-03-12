package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.repository.ValidationKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValidationKeyService {

    //  Second
    private final Long expirationDuration = 120L;

    @Autowired
    private ValidationKeyRepository validationKeyRepository;

    public ValidationKey createNewValidationKey(User user) {

        //  Disable all other validation keys.
        List<ValidationKey> currentValidationKeys = validationKeyRepository.findAllByUser(user);
        for (ValidationKey key : currentValidationKeys) {
            key.setExpirationDate(LocalDateTime.now());
        }

        Long randomKey = null;
        boolean isKeyExists = true;
        while (isKeyExists) {
            randomKey  = ((Double) (Math.random() * 1000000)).longValue();
            Long finalRandomKey = randomKey;
            if (currentValidationKeys
                    .stream()
                    .noneMatch(key ->
                            key.getValidationKey().equals(finalRandomKey))) {
                isKeyExists = false;
            }
        }

        ValidationKey validationKey = new ValidationKey(
                randomKey, LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(expirationDuration),
                user
        );
        return validationKeyRepository.save(validationKey);
    }

}
