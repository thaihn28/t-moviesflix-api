package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.request.LoginRequest;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.payload.response.JwtResponse;

public interface IAuthService {
    JwtResponse login(LoginRequest loginRequest);

}
