package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class MovieResponse {
    private Long id;
    private String name;
    private String content;
    private String type;
    private String thumbURL;
    private String time;
    private String quality;
    private String slug;
    private Integer year;
}
