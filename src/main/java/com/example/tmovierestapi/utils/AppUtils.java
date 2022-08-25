package com.example.tmovierestapi.utils;

import com.example.tmovierestapi.exception.APIException;
import org.springframework.http.HttpStatus;

public class AppUtils {
    public static void validatePageNumberAndSize(int pageNo, int pageSize) {
        if (pageNo < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (pageSize < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (pageSize > Integer.parseInt(AppConstants.MAX_PAGE_SIZE)) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
