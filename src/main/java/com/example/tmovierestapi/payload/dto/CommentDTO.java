package com.example.tmovierestapi.payload.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDTO {
    private Long id;

    @NotBlank(message = "Content must not be empty")
    @Size(min = 1, max = 255, message = "Content must be minimum 1 characters and maximum 255 characters")
    private String content;

}
