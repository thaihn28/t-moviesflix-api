package com.example.tmovierestapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();

        responseBody.put("timestamp", Instant.now());
        responseBody.put("status", status.value());

        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();

        for(FieldError fieldError : fieldErrorList){
            String errorMessage = fieldError.getDefaultMessage();
            errors.add(errorMessage);
        }
        responseBody.put("errors", errors);
        return new ResponseEntity<>(responseBody, headers, status);
    }
}
