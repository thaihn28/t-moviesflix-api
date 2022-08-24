package com.example.tmovierestapi.payload.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


@Data
public class CategoryDtoInMovie {
    @NotEmpty
    private Long id;

    private String name;
}
