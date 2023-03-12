package com.uet.jobfinder.error;

public class RegisterError extends Error {

    public static final RegisterError EMAIL_HAS_BEEN_USED =
            new RegisterError("RGERR1", "Email has been used for another account.");
    public static final RegisterError INVALID_USER_ROLE =
            new RegisterError("RGERR2", "User's role is invalid.");

    public static final RegisterError INCORRECT_VALIDATION_KEY =
            new RegisterError("RGERR3", "Incorrect validation key.");

    public static final RegisterError EXPIRED_VALIDATION_KEY =
            new RegisterError("RGERR4", "Expired validation key.");

    public RegisterError(String code, String message) {
        super(code, message);
    }
}
