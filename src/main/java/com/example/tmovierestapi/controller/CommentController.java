package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Comment;
import com.example.tmovierestapi.payload.dto.CommentDTO;
import com.example.tmovierestapi.payload.response.CommentResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CommentRepository;
import com.example.tmovierestapi.service.ICommentService;
import com.example.tmovierestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "comment")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<PagedResponse<CommentResponse>> getAllComments(
            @RequestParam(value = "movie") Long id,
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    ){
        return new ResponseEntity<>(iCommentService.getAllRootComments(pageNo, pageSize, id), HttpStatus.OK);
    }

    @GetMapping("/{parent}/nested")
    public ResponseEntity<PagedResponse> getChildComments(
            @PathVariable(value = "parent") Long parentId,
            @RequestParam(value = "movie") Long movieId,
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    ){
        return new ResponseEntity<>(iCommentService.getAllChildCommentsByParentId(pageNo, pageSize, movieId, parentId), HttpStatus.OK);
    }

    private static void printChildren(Comment parent, int subLevel) {
        Set<Comment> children = parent.getChildrenComments();

        for (Comment child : children) {
            for (int i = 0; i <= subLevel; i++) System.out.print("--");

            System.out.println(child.getContent());

            printChildren(child, subLevel + 1);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommentDTO> addComment(
            @RequestBody CommentDTO commentDTO,
            @RequestParam(name = "movie") Long movieId,
            @RequestParam(name = "parent-id", required = false) Long parentId
    ){
        return new ResponseEntity<>(iCommentService.addComment(commentDTO, movieId, parentId), HttpStatus.CREATED);
    }
}
