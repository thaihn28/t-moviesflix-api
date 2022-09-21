package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MovieRequest {
    @NotEmpty(message = "Movie ID must not be empty!")
    private Long id;

    private String name;
}
