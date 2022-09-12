package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PlaylistDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200, message = "Playlist name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotBlank
    private String originName;

    @NotBlank(message = "Slug must not be empty!")
    private String slug;

    private int year;

    @Column(name = "thumb_url")
    private String thumbURL;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
