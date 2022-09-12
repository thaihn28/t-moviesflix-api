package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.payload.request.LoginRequest;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.payload.response.JwtResponse;
import com.example.tmovierestapi.service.IAuthService;
import com.example.tmovierestapi.service.IRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IAuthService iAuthService;

    @Autowired
    private IRegistrationService iRegistrationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return new ResponseEntity<>(iAuthService.login(loginRequest), HttpStatus.CREATED);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest signupRequest){
        return new ResponseEntity<>(iRegistrationService.signup(signupRequest), HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return iRegistrationService.confirmToken(token);
    }

    @PostMapping("/admin/sign-up")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> signupAdminAccount(@RequestBody @Valid SignupRequest signupRequest){
        return new ResponseEntity<>(iRegistrationService.signupAdmin(signupRequest), HttpStatus.CREATED);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
}
