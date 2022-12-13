package com.example.tmovierestapi.payload.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class ActorResponse {
    private Long id;
    private String name;
    private String avatar;
    private String slug;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Boolean isHot;
    private Set<MovieResponseInOtherModel> movies = new HashSet<>();

}
