package com.uet.jobfinder.validator;

import com.uet.jobfinder.entity.UserType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleConstraintValidator implements
        ConstraintValidator<RoleConstraint, String> {

    @Override
    public void initialize(RoleConstraint roleConstraint) {
        
    }

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null) {
            return false;
        }

        switch (role) {
            case UserType.CANDIDATE:
            case UserType.COMPANY: return true;
        }

        return false;
    }

}