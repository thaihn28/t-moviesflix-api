package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class EpisodeDTO {
    private Long id;
    @NotEmpty(message = "Episode Name is required")
    private String name;

    @NotEmpty(message = "FileName is required")
    private String filename;

    @NotEmpty(message = "LinkEmbed is required")
    private String linkEmbed;

    @Min(value = 0L, message = "Movie ID is invalid")
    private Long movieID;
}
