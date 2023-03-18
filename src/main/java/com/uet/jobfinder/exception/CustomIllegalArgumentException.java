package com.uet.jobfinder.exception;

import com.uet.jobfinder.error.ServerError;

public class CustomIllegalArgumentException extends IllegalArgumentException {

    public CustomIllegalArgumentException(ServerError error) {
        super(error.getMessage());
        this.error = error;
    }

    private ServerError error;

    public ServerError getError() {
        return error;
    }

    public void setError(ServerError error) {
        this.error = error;
    }
}
