package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class MovieResponseInOtherModel {
    private Long id;
    private Long imdbID;
    private String name;
}
