package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class SearchMovieResponse {
    private Long id;
    private String name;
    private String originName;
    //    private String content;
//    private String type;
    private String thumbURL;
    private String posterURL;
    //    private String time;
//    private String quality;
    private String slug;
    private Integer year;
}
