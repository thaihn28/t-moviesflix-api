package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.model.Comment;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class CommentDTO {
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 255, message = "Content must be minimum 1 characters and maximum 255 characters")
    private String content;

    private Comment commentID;

    private Set<CommentDTO> comments = new HashSet<>();

}
