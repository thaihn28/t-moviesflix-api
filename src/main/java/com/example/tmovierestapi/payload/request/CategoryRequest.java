package com.example.tmovierestapi.payload.request;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


@Data
public class CategoryRequest {
    @NotEmpty
    private Long id;

    private String name;
}
