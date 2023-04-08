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
    MAXIMUM_FILE_EXCEEDED("SVERR10", "File is too large. File size limit is 25MB."),
    FILE_NOT_EXISTS("SVERR11", "File is not exists."),
    INVALID_FILE_TYPE("SVERR12", "Incorrect file type."),

    //  Company error
    NULL_COMPANY_LOGO("SVERR13", "Company logo cannot be null"),

    // Evaluate error
    EVALUATE_NOT_EXITS("EVALEER1", "Evaluate not exists"),
    EVALUATE_EXITS("EVALEER2", "Evaluate exists"),

    // Report error
    REPORT_DESC_NOT_EMPTY("REPORTEER1", "Describe report is empty"),
    REPORT_DESC_NOT_NULL("REPORTEER2", "Describe report is null"),
    REPORT_DATE_NOT_NULL("REPORTEER3", "Date report is null"),
    REPORT_NOT_EXISTED("REPORTEER4", "Report not existed"),

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

    //  Change password error
    WRONG_OLD_PASSWORD("CPERR1", "Old password is wrong."),

    //  Job error
    JOB_NOT_EXISTS("JBERR1", "Job is not exists."),
    COMPANY_NOT_OWN_JOB("JBERR1", "This company not have authority to edit this job."),
    INVALID_JOB_CLOSE_DATE("JBERR2", "Invalid job close date."),
    JOB_CLOSED("JBERR3", "Job has been closed."),

    //  Job application
    CANDIDATE_ALREADY_APPLIED("JAERR1", "Candidate had already applied the job."),
    JOB_APPLICATION_NOT_EXISTS("JAERR2", "Job application not exists."),
    JOB_ALREADY_SAVED("JAERR3", "Candidate has already saved this job."),
    SAVED_JOB_NOT_EXISTS("JBERR4", "Saved job not exists.")
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
