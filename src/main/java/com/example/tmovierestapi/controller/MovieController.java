package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private IMovieService iMovieService;

    @GetMapping
    public ResponseEntity<PagedResponse<Movie>> getAllMovie(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_DATE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<Movie> response = iMovieService.getAllMovies(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody @Valid MovieDTO movieDTO){
        return new ResponseEntity<>(iMovieService.addMovie(movieDTO), HttpStatus.CREATED);
    }

}
