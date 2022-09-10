package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.email.EmailSender;
import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.ERole;
import com.example.tmovierestapi.model.Role;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.registration.token.ConfirmationToken;
import com.example.tmovierestapi.registration.token.ConfirmationTokenService;
import com.example.tmovierestapi.repository.RoleRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.security.services.UserDetailsServiceImpl;
import com.example.tmovierestapi.service.IRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RegistrationService implements IRegistrationService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EmailSender emailSender;

    @Value("${app.authUrl}")
    private String authURL;

    @Override
    public String signup(@Valid SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Username: " + signupRequest.getUsername() + " is already exists!");
        }
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Email: " + signupRequest.getEmail() + " is already exists!");
        }
        // Create new user's account
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<String> rolesRequest = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (rolesRequest == null) {
            Role role = roleRepository.findRoleByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_USER + " is not found"));
            roles.add(role);
        } else {
            rolesRequest.forEach((role -> {
                switch (role.toUpperCase()) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findRoleByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_ADMIN + " is not found"));
                        roles.add(adminRole);
                        user.setEnable(true);
                        break;
                    default:
                        Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_USER + " is not found"));
                        roles.add(userRole);
                        break;
                }
            }
            ));
        }
        user.setRoles(roles);
        userRepository.save(user);

        if(signupRequest.getRoles() == null || signupRequest.getRoles().contains("ROLE_USER")){
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );
            confirmationTokenService.saveConfirmationToken(
                    confirmationToken);
//        TODO: SEND EMAIL
            String link = authURL + "confirm?token=" + token;
            try{
                emailSender.send(
                        signupRequest.getEmail(),
                        signupRequest.fullName(),
                        link
                       );
            }catch (Exception e){
                throw new APIException(HttpStatus.BAD_REQUEST,"Can not send email!");
            }
            return "Registered successfully! An email will be sent in your inbox for confirming the verification, please check it!";
        }
        return "Registered successfully!";
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new APIException(HttpStatus.BAD_REQUEST, token + " not found!")
                );

        if (confirmationToken.getConfirmedAt() != null) {
            return "Email already confirmed";
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        confirmationTokenService.setConfirmedAt(token);
        userDetailsService.enableUser(
                confirmationToken.getUser().getEmail());
        return "Account is verified successfully!";
    }



}
