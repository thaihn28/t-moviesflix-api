package com.example.tmovierestapi.payload.request;

import com.example.tmovierestapi.model.Movie;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EpisodeRequest {
    private Long id;
    @NotEmpty(message = "Episode Name is required")
    private String name;

    @NotEmpty(message = "FileName is required")
    private String filename;

    @NotEmpty(message = "LinkEmbed is required")
    private String linkEmbed;

    @NotEmpty(message = "Movie ID is required")
    private Long movieID;

}
