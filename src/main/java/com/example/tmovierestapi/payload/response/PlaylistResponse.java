package com.example.tmovierestapi.payload.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaylistResponse {
    private Long id;
    private String name;
    private String originName;
    private String thumbURL;
    private int year;
    private String type;
    private String slug;
}
