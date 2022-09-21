package com.example.tmovierestapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EpisodeRequest {
    private Long id;
    @NotEmpty(message = "Episode Name must not be empty")
    private String name;

    @NotEmpty(message = "FileName must not be empty")
    private String filename;

    @NotEmpty(message = "LinkEmbed must not be empty")
    private String linkEmbed;

    @NotEmpty(message = "Movie ID must not be empty")
    private Long movieID;

}
