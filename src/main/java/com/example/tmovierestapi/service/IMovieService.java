package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IMovieService {
    PagedResponse<Movie> getAllMovies(int pageNo, int pageSize, String sortDir, String sortBy);
    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile);
    PagedResponse<Movie> getMoviesByCategory(Long categoryID, int pageNo, int pageSize, String sortDir, String sortBy);
    MovieDTO updateMovie(Long id, MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile);
    void deleteMovie(Long id);
    Movie getMovieBySlug(String slug);
    PagedResponse<Movie> getMoviesByActor(Long actorID, int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Movie> getMoviesByDirector(Long directorID, int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Movie> getMoviesByType(String type,  int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Movie> getAllHotMovies(int pageNo, int pageSize, String sortDir, String sortBy);
}
