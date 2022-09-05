package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.payload.request.MovieRequest;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class ActorDTO {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 200, message = "Category name must be minimum 2 characters and maximum 200 characters")
    private String name;

    private String avatar;

    private Boolean isHot;

//    private Set<MovieRequest> movies = new HashSet<>();
}
