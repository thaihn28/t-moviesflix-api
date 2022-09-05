package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class EpisodeDTO {
    private Long id;
    @NotEmpty(message = "Episode Name is required")
    private String name;

    @NotEmpty(message = "FileName is required")
    @Size(min = 2, max = 200, message = "FileName must be minimum 2 characters and maximum 200 characters")
    private String filename;

    @NotEmpty(message = "LinkEmbed is required")
    @Size(min = 10, max = 200, message = "LinkEmbed must be minimum 10 characters and maximum 200 characters")
    private String linkEmbed;

    @Min(value = 0L, message = "Movie ID is invalid")
    private Long movieID;
}
