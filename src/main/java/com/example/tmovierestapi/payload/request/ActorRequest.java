package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ActorRequest {
    @NotEmpty(message = "Actor ID is required")
    private Long id;

    private String name;
}
