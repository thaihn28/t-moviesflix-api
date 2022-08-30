package com.example.tmovierestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TMovieRestApiApplication {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        System.setProperty("server.error.include-stacktrace", "never");

        SpringApplication.run(TMovieRestApiApplication.class, args);
    }
    // Run when spring boot app is started
    @Bean
    CommandLineRunner run() {
        return args -> {

        };
    }

}
