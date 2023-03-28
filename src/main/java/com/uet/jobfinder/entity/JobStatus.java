package com.uet.jobfinder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum JobStatus {
    OPEN, CLOSE
}
