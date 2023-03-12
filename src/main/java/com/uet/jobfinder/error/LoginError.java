package com.uet.jobfinder.error;

public class LoginError extends Error {
    public static final LoginError WRONG_PASSWORD_OR_USERNAME =
            new LoginError("LGERR1", "Username or password is incorrect.");

    public static final LoginError LOGIN_EMAIL_NOT_EXISTS =
            new LoginError("LGERR2", "Email is not exists.");

    public LoginError(String code, String message) {
        super(code, message);
    }
}
