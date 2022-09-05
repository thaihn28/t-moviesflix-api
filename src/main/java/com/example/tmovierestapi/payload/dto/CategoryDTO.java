package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.payload.request.MovieRequest;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200, message = "Category name must be minimum 2 characters and maximum 200 characters")
    private String name;

//    private Set<MovieRequest> movies = new HashSet<>();
}
