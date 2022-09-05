package com.example.tmovierestapi;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@SpringBootApplication
public class TMovieRestApiApplication {
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
//            System.out.println(LocalDateTime.now());
        };
    }

}
