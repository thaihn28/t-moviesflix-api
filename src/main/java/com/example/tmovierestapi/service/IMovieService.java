package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.stereotype.Service;

public interface IMovieService {
    PagedResponse<Movie> getAllMovies(int pageNo, int pageSize, String sortDir, String sortBy);
    MovieDTO addMovie(MovieDTO movieDTO);

}
