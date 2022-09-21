package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.dto.CommentDTO;

public interface ICommentService {
    CommentDTO addComment(CommentDTO commentDTO);
}
