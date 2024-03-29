package com.example.tmovierestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Long fieldValueInt;
    private String fieldValueStr;

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValueInt) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValueInt));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueInt = fieldValueInt;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValueStr) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValueStr));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueStr = fieldValueStr;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getFieldValueInt() {
        return fieldValueInt;
    }

    public String getFieldValueStr() {
        return fieldValueStr;
    }
}
