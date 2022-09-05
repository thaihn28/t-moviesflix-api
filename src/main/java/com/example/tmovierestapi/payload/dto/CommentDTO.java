package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class CommentDTO {
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 255, message = "Content must be minimum 1 characters and maximum 255 characters")
    private String content;

    private Instant createdDate;

    @Min(value = 0L, message = "User ID is invalid")
    private Long userID;

    @Min(value = 0L, message = "Movie ID is invalid")
    private Long movieID;

}
