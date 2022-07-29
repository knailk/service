package com.audit.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseErrDetail {
    private int status;
    private Date timestamp;
    private String message;
    private String description;
    public BaseErrDetail(int status, Date timestamp, String message, String description) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
      
}
