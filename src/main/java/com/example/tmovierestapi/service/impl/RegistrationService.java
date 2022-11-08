package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.email.EmailSender;
import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.enums.ERole;
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
@Transactional
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

    private final String ADMIN_ACCOUNT = "hoangthai.solem2801@gmail.com";

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

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findRoleByName(ERole.USER)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.USER + " is not found"));
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                user
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
//        TODO: SEND EMAIL
        String link = authURL + "confirm?token=" + token;
        try {
            emailSender.send(
                    signupRequest.getEmail(),
                    signupRequest.fullName(),
                    link
            );
        } catch (Exception e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Can not send email!");
        }
        return "Registered successfully! An email will be sent to inbox in your email:" + signupRequest.getEmail() +" for confirming the verification, please check it!";
    }

    @Override
    public String signupAdmin(SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Username: " + signupRequest.getUsername() + " is already exists!");
        }
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Email: " + signupRequest.getEmail() + " is already exists!");
        }
        // Create new user's account
        User admin = new User();
        admin.setUsername(signupRequest.getUsername());
        admin.setFirstName(signupRequest.getFirstName());
        admin.setLastName(signupRequest.getLastName());
        admin.setEmail(signupRequest.getEmail());
        admin.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findRoleByName(ERole.ADMIN)
                        .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Can not found ROLE ADMIN"));
        roles.add(role);

        admin.setRoles(roles);
        userRepository.save(admin);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                admin
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
//        TODO: SEND EMAIL
        String link = authURL + "confirm?token=" + token;
        try {
            emailSender.send(
                    ADMIN_ACCOUNT,
                    signupRequest.fullName(),
                    link
            );
        } catch (Exception e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Can not send email!");
        }
        return "Registered admin account successfully!";
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
