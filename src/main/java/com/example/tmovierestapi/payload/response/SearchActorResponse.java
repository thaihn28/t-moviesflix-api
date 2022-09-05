package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class SearchActorResponse {
    private Long id;
    private String name;
    private String avatar;
}
