package com.example.tmovierestapi.utils;

import java.util.regex.Pattern;

public class AppConstants {
    public static final String MAX_PAGE_SIZE = "20";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String SORT_BY_YEAR = "year";

    public static final String SORT_BY_CREATED_DATE = "createdDate";
    public static final String SORT_BY_NAME = "name";
    public static final String SORT_DIRECTION = "asc";
    public static final Pattern VALID_EMAIL
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
