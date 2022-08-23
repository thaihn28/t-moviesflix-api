package com.example.tmovierestapi.payload.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PlaylistDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200, message = "Movie name must be minium 2 characters and maximum 200 characters")
    private String name;

    @NotBlank
    private String originName;

    @NotBlank(message = "Slug must not be empty!")
    private String slug;

    private int year;

    @NotBlank
    @Column(name = "thumb_url")
    private String thumbURL;

}
