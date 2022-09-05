package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class CountryDTO {
    private Long id;

    @NotEmpty(message = "Country name is required")
    private String name;

    private LocalDateTime createdDate;
}
