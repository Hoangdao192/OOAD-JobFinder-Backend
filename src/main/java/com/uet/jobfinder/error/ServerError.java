package com.uet.jobfinder.error;

public enum ServerError {

    //  Common error
    EMAIL_NOT_EXISTS("AUERR1", "Email is not exists."),
    EMAIL_NOT_VALID("AUERR2", "Invalid email."),
    SERVER_ERROR("SVERR1", "Server errors."),

    //  Login error
    WRONG_PASSWORD_OR_USERNAME("LGERR1", "Username or password is incorrect."),
    EMAIL_NOT_EMPTY("LGERR2", "Email cannot be empty."),
    EMAIL_NOT_NULL("LGERR3", "Email cannot be null."),
    PASSWORD_NOT_EMPTY("LGERR4", "Password cannot be empty."),
    PASSWORD_NOT_NULL("LGERR5", "Password cannot be null."),

    //  Register error
    EMAIL_HAS_BEEN_USED("RGERR1", "Email has been used for another account."),
    INVALID_USER_ROLE("RGERR2", "User's role is invalid."),
    INCORRECT_VALIDATION_KEY("RGERR3", "Incorrect validation key."),
    EXPIRED_VALIDATION_KEY("RGERR4", "Expired validation key."),
    INVALID_ROLE("RGERR5", "Invalid role")
    ;

    private String code;
    private String message;

    ServerError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
