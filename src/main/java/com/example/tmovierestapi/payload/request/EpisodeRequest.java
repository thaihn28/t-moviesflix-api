package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EpisodeRequest {
    @NotEmpty(message = "Episode ID is required")
    private Long id;

    private String name;
    private String linkEmbed;
}
