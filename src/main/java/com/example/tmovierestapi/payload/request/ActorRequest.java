package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ActorRequest {
    @NotEmpty(message = "Actor ID must not be empty")
    private Long id;

    private String name;
}
