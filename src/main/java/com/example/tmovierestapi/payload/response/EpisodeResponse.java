package com.example.tmovierestapi.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodeResponse {

    private Long id;

    private String name;

    private String filename;

    private String linkEmbed;

    private MovieResponseInOtherModel movie;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
