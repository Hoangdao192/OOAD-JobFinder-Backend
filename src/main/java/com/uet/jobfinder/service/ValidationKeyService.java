package com.uet.jobfinder.service;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.entity.ValidationKey;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import com.uet.jobfinder.repository.ValidationKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValidationKeyService {

    //  Second
    private static final Long EXPIRATION_DURATION = 120L;

    private ValidationKeyRepository validationKeyRepository;

    public ValidationKey createNewValidationKey(User user) {

        //  Disable all other validation keys.
        List<ValidationKey> currentValidationKeys = validationKeyRepository.findAllByUser(user);
        for (ValidationKey key : currentValidationKeys) {
            key.setExpirationDate(LocalDateTime.now());
        }

        Long randomKey = ((Double) (Math.random() * 1000000)).longValue();
        boolean isKeyExists = true;
        while (isKeyExists) {
            randomKey  = ((Double) (Math.random() * 1000000)).longValue();
            Long finalRandomKey = randomKey;
            if (currentValidationKeys
                    .stream()
                    .noneMatch(key ->
                            key.getValidationKey().equals(finalRandomKey.toString()))) {
                isKeyExists = false;
            }
        }

        ValidationKey validationKey = new ValidationKey(
                randomKey.toString(), LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(EXPIRATION_DURATION),
                user
        );
        return validationKeyRepository.save(validationKey);
    }

    public ValidationKey findByUserAndValidationKey(
            User user,
            String validationKey
    ) {
        return validationKeyRepository.findByUserAndValidationKey(
                user, validationKey
        ).orElseThrow(() ->
                new CustomIllegalArgumentException(ServerError.INCORRECT_VALIDATION_KEY));
    }

    public void activeValidationKey(ValidationKey validationKey) {
        validationKey.setActivated(true);
        validationKey.setExpirationDate(LocalDateTime.now());
        validationKeyRepository.save(validationKey);
    }

    /**
     * If a validation key is activated or expired
     */
    public boolean isValidationKeyExpired(ValidationKey validationKey) {
        return validationKey.isActivated()
                || LocalDateTime.now().isAfter(validationKey.getExpirationDate());
    }

    @Autowired
    public void setValidationKeyRepository(ValidationKeyRepository validationKeyRepository) {
        this.validationKeyRepository = validationKeyRepository;
    }
}
