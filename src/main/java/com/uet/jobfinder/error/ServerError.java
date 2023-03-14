package com.uet.jobfinder.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ServerError {

    //  Common error
    EMAIL_NOT_EXISTS("AUERR1", "Email is not exists."),
    EMAIL_NOT_VALID("AUERR2", "Invalid email."),
    SERVER_ERROR("SVERR1", "Server errors."),
    NOT_EMPTY("SVERR3", "Cannot be empty."),
    NOT_NULL("SVERR4", "Cannot be null."),
    ACCESS_DENIED("SVERR5", "You do not have authority to do this."),
    USER_ID_NOT_EXISTS("SVERR6", "User with ID not exists."),
    COMPANY_NOT_EXISTS("SVERR7", "Company not exists."),
    INVALID_REQUEST("SVERR8", "Invalid request."),
    INVALID_AUTHORIZATION("SVERR9", "Invalid authorization"),

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
    INVALID_ROLE("RGERR5", "Invalid role"),

    //  Job error
    JOB_NOT_EXISTS("JBERR1", "Job is not exists."),
    COMPANY_NOT_OWN_JOB("JBERR1", "This company not have authority to edit this job.")
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
