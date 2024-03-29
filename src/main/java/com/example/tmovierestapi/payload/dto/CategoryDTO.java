package com.example.tmovierestapi.payload.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name must not be empty")
    @Size(min = 2, max = 200, message = "Category name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotBlank(message = "Slug must not be empty")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

//    private Set<MovieRequest> movies = new HashSet<>();
}
