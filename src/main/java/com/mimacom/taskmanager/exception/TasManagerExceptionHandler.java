package com.mimacom.taskmanager.exception;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class TasManagerExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, ValidationException.class, HttpMessageNotReadableException.class,
            PropertyValueException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception exception, HttpServletRequest request) {

        TaskManagerException taskManagerException = TaskManagerException.builder().message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(taskManagerException, taskManagerException.getStatus());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public final ResponseEntity<Object> handleEntityNotFoundException(Exception exception,
                                                                      HttpServletRequest request) {
        TaskManagerException taskManagerException = TaskManagerException.builder().message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND).code(HttpStatus.NOT_FOUND.value()).timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(taskManagerException, taskManagerException.getStatus());
    }
}
