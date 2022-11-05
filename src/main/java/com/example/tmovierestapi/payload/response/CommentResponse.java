package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.payload.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;

    private String content;

    private int replyCount;

    private UserDTO user;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
