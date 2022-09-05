package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.model.Movie;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodeResponse {

    private Long id;

    private String name;

    private String filename;

    private String linkEmbed;

    private MovieResponse movie;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
