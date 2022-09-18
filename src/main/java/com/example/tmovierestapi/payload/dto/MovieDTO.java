package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.model.*;
import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 200, message = "Movie name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotEmpty
    @Size(min = 2, max = 200, message = "Movie origin name must be minimum 2 characters and maximum 200 characters")
    private String originName;

    @NotEmpty(message = "Content should not be null or empty")
    private String content;

    @NotBlank
    private String type;

    private String thumbURL;

    @NotBlank
    private String trailerURL;

    @NotBlank
    private String time;

    @NotBlank
    private String episodeCurrent;

    @NotBlank
    private String episodeTotal;

    @NotBlank
    private String quality;

    @NotBlank
    private String slug;

    private String posterURL;

    private Integer year;

    private String showTimes;

    private Boolean isHot;

    private Boolean isPremium;

    private Double price;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    // Country
    private String countrySlug;

    private Set<ActorRequest> actors = new HashSet<>();

    private Set<DirectorRequest> directors = new HashSet<>();

    private Set<CategoryRequest> categories = new HashSet<>();

    private Set<Episode> episodes = new HashSet<>();

    private Set<Comment> comments = new HashSet<>();

}
