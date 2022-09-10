package com.example.tmovierestapi;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.tmovierestapi.model.ERole;
import com.example.tmovierestapi.model.Role;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.repository.RoleRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.service.IAuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class TMovieRestApiApplication {
    @Autowired
    private IAuthService iAuthService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${com.cloudinary.cloud_name}")
    private String cloudName;

    @Value("${com.cloudinary.api_key}")
    private String apiKey;

    @Value("${com.cloudinary.api_secret}")
    private String apiSecret;


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    //    Here's an example of setting the configuration parameters programatically:
    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
        cloudinary.url().transformation(
                new Transformation().height(250).width(250).crop("fill"));
        return cloudinary;
    }

    public static void main(String[] args) {
        System.setProperty("server.error.include-stacktrace", "never");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
        SpringApplication.run(TMovieRestApiApplication.class, args);
    }

    // Run when spring boot app is started
    @Bean
    CommandLineRunner run() {
        return args -> {
            Role roleAdmin = new Role();
            roleAdmin.setName(ERole.ROLE_ADMIN);
            if(!roleRepository.existsRoleByName(roleAdmin.getName())){
                roleRepository.save(roleAdmin);
            }

//            System.out.println(LocalDateTime.now());
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail("thaihn@gmail.com");
            signupRequest.setUsername("thaihn");
            signupRequest.setPassword("hnt282001");
            Set<String> roles = new HashSet<>();
            roles.add("admin");
            signupRequest.setRoles(roles);
            if(!userRepository.existsUserByEmail(signupRequest.getEmail()) ||
                    !userRepository.existsUserByUsername(signupRequest.getUsername())){
                iAuthService.signup(signupRequest);
            }
        };
    }

}
