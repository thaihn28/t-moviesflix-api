package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class MovieResponse {
    private Long id;
    private Long imdbID;
    private String name;
    private String originName;
    private String thumbURL;
    private Integer year;
    private String type;
    private String slug;
}
