package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentMovieResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IMovieService {
    PagedResponse<Movie> getAllMovies(int pageNo, int pageSize, String sortDir, String sortBy);
    MovieDTO addMovie(MovieDTO movieDTO);
    MovieDTO addMovieWithUploadFile(MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile);
    PagedResponse<Movie> getMoviesByCategory(Set<String> slugs, int pageNo, int pageSize, String sortDir, String sortBy);
    MovieDTO updateMovie(Long id, MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile);
    MovieDTO updatePartialMovieField(Long id, MovieDTO movieDTO);
    void deleteMovie(Long id);
    PaymentMovieResponse getMovieBySlug(String slug);
    PagedResponse<MovieResponse> getMoviesByActor(String slug, int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<MovieResponse> getMoviesByDirector(String slug, int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<MovieResponse> getMoviesByType(String type,  int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<MovieResponse> getAllHotMovies(int pageNo, int pageSize, String sortDir, String sortBy);
}
