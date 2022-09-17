package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FavoriteDTO {
    private Long id;

    @NotBlank(message = "Playlist Slug must not be blank")
    private String slug;

}
