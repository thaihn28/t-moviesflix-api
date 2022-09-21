package com.example.tmovierestapi.payload.request;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


@Data
public class CategoryRequest {
    @NotEmpty(message = "Category ID must not be empty")
    private Long id;

    private String name;
}
