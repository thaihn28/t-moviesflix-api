package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.payload.request.CategoryRequest;
import lombok.Data;

import javax.validation.constraints.*;
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
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private int year;

    @NotNull(message = "Is Hot must not be blank")
    private Boolean isHot = false;

    @NotNull(message = "Is Premium must not be blank")
    private Boolean isPremium;

    @NotNull(message = "Category must not be blank")
    private Set<CategoryRequest> categories = new HashSet<>();

    @NotBlank(message = "Country name must not be blank")
    private String countryName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

}
