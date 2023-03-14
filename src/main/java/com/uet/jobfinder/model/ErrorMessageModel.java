package com.uet.jobfinder.model;

import com.uet.jobfinder.error.ServerError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageModel {

    private List<ServerError> errors = new ArrayList<>();

    public void addError(ServerError error) {
        errors.add(error);
    }

}
