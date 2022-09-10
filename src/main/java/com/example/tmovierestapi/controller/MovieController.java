package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private IMovieService iMovieService;

    @GetMapping
    public ResponseEntity<PagedResponse<Movie>> getAllMovies(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<Movie> response = iMovieService.getAllMovies(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Movie> getMovieBySlug(@PathVariable(value = "slug") String slug){
        return new ResponseEntity<>(iMovieService.getMovieBySlug(slug), HttpStatus.OK);
    }

//    @PostMapping("/add")
    @RequestMapping(path = "/add", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MovieDTO> addMovie(@ModelAttribute(name = "movie") @Valid MovieDTO movieDTO,
                                             @RequestPart(name = "thumbFile") @ValidImage MultipartFile thumbFile,
                                             @RequestPart(name = "posterFile") @ValidImage MultipartFile posterFile

    ) {
        return new ResponseEntity<>(iMovieService.addMovie(movieDTO, thumbFile, posterFile), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable(value = "id") Long id,@RequestBody @Valid MovieDTO movieDTO){
        return new ResponseEntity<>(iMovieService.updateMovie(id, movieDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable(value = "id") Long id) {
        iMovieService.deleteMovie(id);
        return new ResponseEntity<>("Deleted movie with ID-" + id + " successfully!", HttpStatus.OK);
    }

    @GetMapping("/filter-by-category/{id}")
    public ResponseEntity<PagedResponse<Movie>> getMoviesByCategory(@PathVariable(value = "id") Long cateID,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByCategory(cateID, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-actor/{id}")
    public ResponseEntity<PagedResponse<Movie>> getMoviesByActor(@PathVariable(value = "id") Long actorID,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByActor(actorID, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-director/{id}")
    public ResponseEntity<PagedResponse<Movie>> getMoviesByDirector(@PathVariable(value = "id") Long directorID,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByDirector(directorID, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }
    @GetMapping("/filter-by-type")
    public ResponseEntity<PagedResponse<Movie>> getMoviesByType(@RequestParam(value = "type") String type,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByType(type, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }


}
