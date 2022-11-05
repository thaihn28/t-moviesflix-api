package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Comment;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.dto.CommentDTO;
import com.example.tmovierestapi.payload.dto.UserDTO;
import com.example.tmovierestapi.payload.response.CommentResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CommentRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.ICommentService;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CommentDTO addComment(CommentDTO commentDTO, Long movieID, Long parentID) {
        Comment convertToEntity = modelMapper.map(commentDTO, Comment.class);

        CommentDTO commentResponse = null;

        Movie movie = movieRepository.findMovieById(movieID)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", " not found"));

        Authentication authentication = AppGetLoggedIn.getLoggedIn();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(customUserDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username:" + customUserDetails.getUsername() + " not found!"));

            if (parentID != null) {
                /*TODO: Find parent comment by id
                   -> check if exist
                   -> update this comment by add list child comment
                */
                Optional<Comment> existComment = commentRepository.findCommentById(parentID);

                if (existComment.isPresent()) {
                    Comment getComment = existComment.get();

                    Comment childComment = new Comment();

                    childComment.setContent(convertToEntity.getContent());
                    childComment.setCreatedDate(LocalDateTime.now());
                    childComment.setMovie(movie);
                    childComment.setUser(currentUser);
                    childComment.setParentComment(getComment);

                    getComment.addChild(childComment);

                    Comment request = commentRepository.save(childComment);
                    commentResponse = modelMapper.map(request, CommentDTO.class);
                }
            } else {
                Comment commentRequest = new Comment();
                commentRequest.setContent(convertToEntity.getContent());
                commentRequest.setParentComment(null);
                commentRequest.setCreatedDate(LocalDateTime.now());
                commentRequest.setUser(currentUser);
                commentRequest.setMovie(movie);
                commentRequest.setChildrenComments(null);

                Comment comment = commentRepository.save(commentRequest);

                commentResponse = modelMapper.map(comment, CommentDTO.class);
            }
        }
        return commentResponse;
    }

    @Override
    public PagedResponse<CommentResponse> getAllRootComments(int pageNo, int pageSize, Long movieId) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Comment> commentPage = commentRepository.findAllRoots(movieId, pageable);

        List<Comment> rootComments = commentPage.getNumberOfElements() == 0 ? Collections.emptyList() : commentPage.getContent();

        List<CommentResponse> commentsDTO = new ArrayList<>();
        // Now Find all the subcategories
//        List<Long> rootCategoryIds = rootCategories.stream().map(Comment::getId).collect(Collectors.toList());
//        List<Comment> subCategories = commentRepository.findAllSubCategoriesInRoot(rootCategoryIds); // second db call
//
//        for(Comment sub : subCategories){
//            sub.getParentComment().getChildrenComments().add(sub);
//        }
        for (Comment c : rootComments) {
            User user = c.getUser();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getUsername()
            );
            CommentResponse commentResponse = new CommentResponse(
                    c.getId(),
                    c.getContent(),
                    c.getChildrenComments().size(),
                    userDTO,
                    c.getCreatedDate(),
                    c.getModifiedDate()
            );
            commentsDTO.add(commentResponse);
        }
        return new PagedResponse<>(commentsDTO, commentPage.getNumber(), commentPage.getSize(),
                commentPage.getTotalElements(), commentPage.getTotalPages(), commentPage.isLast()
        );
    }

    @Override
    public PagedResponse<CommentResponse> getAllChildCommentsByParentId(int pageNo, int pageSize, Long movieId, Long parentId) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Comment> commentPage = commentRepository.findAllChildCommentsByParentId(movieId, parentId, pageable);

        List<Comment> subComments = commentPage.getNumberOfElements() == 0 ? Collections.emptyList() : commentPage.getContent();

        List<CommentResponse> subCommentsDTO = new ArrayList<>();

        for (Comment c : subComments) {
            User user = c.getUser();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getUsername()
            );
            CommentResponse commentResponse = new CommentResponse(
                    c.getId(),
                    c.getContent(),
                    c.getChildrenComments().size(),
                    userDTO,
                    c.getCreatedDate(),
                    c.getModifiedDate()
            );
            subCommentsDTO.add(commentResponse);
        }
        return new PagedResponse<>(subCommentsDTO, commentPage.getNumber(), commentPage.getSize(),
                commentPage.getTotalElements(), commentPage.getTotalPages(), commentPage.isLast()
        );
    }
}
