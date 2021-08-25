package com.mimacom.taskmanager.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskManagerException {

    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;
    private int code;
}
