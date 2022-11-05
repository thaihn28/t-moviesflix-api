package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Comment;
import com.example.tmovierestapi.payload.dto.CommentDTO;
import com.example.tmovierestapi.payload.response.CommentResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;

import java.util.List;

public interface ICommentService {
    CommentDTO addComment(CommentDTO commentDTO, Long movieID, Long parentID);
    PagedResponse<CommentResponse> getAllRootComments(int pageNo, int pageSize, Long movieId);
    PagedResponse<CommentResponse> getAllChildCommentsByParentId(int pageNo, int pageSize, Long movieId, Long parentId);
}
