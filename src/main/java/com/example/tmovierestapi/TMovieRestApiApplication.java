package com.example.tmovierestapi;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.tmovierestapi.enums.ERole;
import com.example.tmovierestapi.model.Role;
import com.example.tmovierestapi.payload.request.SignupRequest;
import com.example.tmovierestapi.repository.RoleRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.service.IRegistrationService;
import com.example.tmovierestapi.utils.admin.Password;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableCaching
public class TMovieRestApiApplication {
    @Autowired
    private IRegistrationService iRegistrationService;

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
//            try {
//                emailSender.send("hoangthai.solem2801@gmail.com", "Thai", "GGGG");
//            }catch (Exception e){
//                throw new Exception(e.getMessage());
//            }
            Boolean isExistRole = roleRepository.existsRoleByName(ERole.ADMIN);
            if(!isExistRole){
                roleRepository.save(new Role(ERole.ADMIN));
            }
            Boolean isExistRoleUser = roleRepository.existsRoleByName(ERole.USER);
            if(!isExistRoleUser){
                roleRepository.save(new Role(ERole.USER));
            }
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail("tmovie.email@gmail.com");
            signupRequest.setUsername("admin1");
            signupRequest.setLastName("1");
            signupRequest.setFirstName("ADMIN");
            signupRequest.setPassword(Password.HIDDEN);

            Boolean existsUserByEmail = userRepository.existsUserByEmail(signupRequest.getEmail());
            Boolean existsUserByUsername = userRepository.existsUserByUsername(signupRequest.getUsername());
            if(!existsUserByEmail && !existsUserByUsername){
                iRegistrationService.signupAdmin(signupRequest);
            }
        };
    }

}
