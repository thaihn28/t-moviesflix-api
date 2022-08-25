package com.example.tmovierestapi.payload.converter;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


@Data
public class CategoryInMovieDTO {
    @NotEmpty
    private Long id;

    private String name;
}
