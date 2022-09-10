package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.request.SignupRequest;

public interface IRegistrationService {
    String signup(SignupRequest signupRequest);

    String confirmToken(String token);
    String buildEmail(String name, String link);
}
