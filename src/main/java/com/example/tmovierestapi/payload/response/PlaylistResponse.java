package com.example.tmovierestapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {
    private Long id;
    private String name;
    private String originName;
    private String thumbURL;
    private int year;
    private String type;
    private String slug;
}
