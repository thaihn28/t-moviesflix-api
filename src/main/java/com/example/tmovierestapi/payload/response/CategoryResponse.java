package com.example.tmovierestapi.payload.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryResponse {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200, message = "Category name must be minimum 2 characters and maximum 200 characters")
    private String name;

    private String slug;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private Set<MovieResponse> movies = new HashSet<>();
}
