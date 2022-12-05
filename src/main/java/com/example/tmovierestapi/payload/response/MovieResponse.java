package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class MovieResponse {
    private Long id;
    private Long imdbID;
    private String name;
    private String originName;
    private String thumbURL;
    private String posterURL;
    private String trailerURL;
    private Integer year;
    private String type;
    private String slug;
    private Boolean isHot;
    private Boolean isPremium;
    private Double price;
}
