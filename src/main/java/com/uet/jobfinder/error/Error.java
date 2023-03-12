package com.uet.jobfinder.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    public enum ErrorCode {
        AUERR1,
        SEVRR1
    }


    public static final Error EMAIL_NOT_EXISTS =
            new Error("AUERR1", "Email is not exists.");
    public static final Error SERVER_ERROR =
            new Error("SVERR1", "Server errors.");

    private String code;
    private String message;

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
