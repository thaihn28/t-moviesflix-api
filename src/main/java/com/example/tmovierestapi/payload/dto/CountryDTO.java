package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class CountryDTO {
    private Long id;

    @NotEmpty(message = "Country name is required")
    private String name;

    @NotEmpty(message = "Slug must not be empty")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private LocalDateTime createdDate;
}
