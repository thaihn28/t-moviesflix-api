package com.example.tmovierestapi.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppGetLoggedIn {
    public static Authentication getLoggedIn(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }
}
