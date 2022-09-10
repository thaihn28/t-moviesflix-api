package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.ERole;
import com.example.tmovierestapi.model.Role;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.request.LoginRequest;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.payload.response.JwtResponse;
import com.example.tmovierestapi.repository.RoleRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.security.jwt.JwtUtils;
import com.example.tmovierestapi.security.services.UserDetailsImpl;
import com.example.tmovierestapi.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public String signup(SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Username: " + signupRequest.getUsername() + " is already exists!");
        }
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Error: Email: " + signupRequest.getEmail() + " is already exists!");
        }
        // Create new user's account
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<String> rolesRequest = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (rolesRequest == null) {
            Role role = roleRepository.findRoleByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_USER + "is not found"));
            roles.add(role);
        } else {
            rolesRequest.forEach((role -> {
                switch (role.toUpperCase()) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findRoleByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_ADMIN + "is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Error:" + ERole.ROLE_USER + "is not found"));
                        roles.add(userRole);
                        break;
                }
            }
            ));
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }
}
