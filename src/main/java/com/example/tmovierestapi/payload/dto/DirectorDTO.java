package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class DirectorDTO {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 200, message = "Category name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotEmpty(message = "Slug is required")
    private String slug;

    private String avatar;

    private Boolean isHot;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

//    private Set<MovieRequest> movies = new HashSet<>();
}
