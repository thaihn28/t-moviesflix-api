package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;

    @NotNull(message = "Imdb id must be greater than 0")
    private Long imdbID;

    @NotBlank(message = "Movie name must not be empty")
    @Size(min = 2, max = 200, message = "Movie name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotBlank
    @Size(min = 2, max = 200, message = "Movie origin name must be minimum 2 characters and maximum 200 characters")
    private String originName;

    @NotBlank(message = "content should not be null or empty")
    @Size(max = 10000, message = "Content cannot greater than 4000 letter")
    private String content;

    @NotBlank(message = "type should not be null or empty")
    private String type;

    private String thumbURL;

    @NotBlank(message = "trailerURL should not be null or empty")
    private String trailerURL;

    private String time;

    private String quality;

    @NotBlank(message = "Slug must not be empty")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private String posterURL;

    private Integer year;

    private Boolean isHot;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @NotNull(message = "Price must not be null")
    private Double price;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    // Country
    private String countrySlug;

    private Set<ActorRequest> actors = new HashSet<>();

    private Set<DirectorRequest> directors = new HashSet<>();

    private Set<CategoryRequest> categories = new HashSet<>();

}
