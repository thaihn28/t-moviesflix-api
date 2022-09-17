package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PlaylistDTO {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 200, message = "Movie name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotBlank
    @Size(min = 2, max = 200, message = "Movie origin name must be minimum 2 characters and maximum 200 characters")
    private String originName;

    @NotBlank(message = "Type must not be empty")
    private String type;


    @NotBlank(message = "Episode current must not be empty")
    private String episodeCurrent;

    @NotBlank(message = "Time must not be empty")
    private String time;

    @NotBlank(message = "Quality must not be empty")
    private String quality;

    @NotBlank(message = "Slug must not be empty")
    private String slug;

    private int year;

    @NotNull(message = "Is Hot must not be blank")
    private Boolean isHot = false;

    @NotNull(message = "Is Premium must not be blank")
    private Boolean isPremium;

    private Set<CategoryRequest> categories = new HashSet<>();

    @NotBlank(message = "Country name must not be blank")
    private String countryName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

}
