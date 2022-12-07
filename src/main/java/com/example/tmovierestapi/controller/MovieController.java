package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentMovieResponse;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.utils.AppConstants;
import com.example.tmovierestapi.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/movies")
@Tag(name = "movie")
@SecurityRequirement(name = "tmovieapi")
public class MovieController {
    @Autowired
    private IMovieService iMovieService;

    @Autowired
    private AppUtils appUtils;

    @Operation(description = "View all list movies", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Movie.class))), responseCode = "200")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

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
    public ResponseEntity<PaymentMovieResponse> getMovieBySlug(@PathVariable(value = "slug") String slug) {
        return new ResponseEntity<>(iMovieService.getMovieBySlug(slug), HttpStatus.OK);
    }

    @RequestMapping(path = "/add/upload", method = POST,
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
            ,produces = {MediaType.APPLICATION_JSON_VALUE}
    )

    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDTO> addMovieWithUploadFile(@RequestPart(name = "movie") @Valid MovieDTO movieDTO,
                                             @RequestPart(name = "thumbFile") @ValidImage MultipartFile thumbFile,
                                             @RequestPart(name = "posterFile") @ValidImage MultipartFile posterFile

    ) {
        MovieDTO response = iMovieService.addMovieWithUploadFile(movieDTO, thumbFile, posterFile);
        appUtils.notifyNewMovie(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody @Valid MovieDTO movieDTO

    ) {
        MovieDTO response = iMovieService.addMovie(movieDTO);
        appUtils.notifyNewMovie(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovieWithUploadFile(@PathVariable(value = "id") Long id,
                                                @RequestPart(name = "movie") @Valid MovieDTO movieDTO,
                                                @RequestPart(name = "thumbFile", required = false) @ValidImage MultipartFile thumbFile,
                                                @RequestPart(name = "posterFile", required = false) @ValidImage MultipartFile posterFile
    ) {
        return new ResponseEntity<>(iMovieService.updateMovie(id, movieDTO, thumbFile, posterFile), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid MovieDTO movieDTO
    ) {
        return new ResponseEntity<>(iMovieService.updatePartialMovieField(id, movieDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable(value = "id") Long id) {
        iMovieService.deleteMovie(id);
        return new ResponseEntity<>("Deleted movie with ID-" + id + " successfully!", HttpStatus.OK);
    }

    @GetMapping("/filter-by-category")
    public ResponseEntity<PagedResponse<Movie>> getMoviesByCategory(@RequestParam(value = "slug") Set<String> slugs,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByCategory(slugs, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-actor/{slug}")
    public ResponseEntity<PagedResponse<MovieResponse>> getMoviesByActor(@PathVariable(value = "slug") String slug,
                                                                         @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                         @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                         @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByActor(slug, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-director/{slug}")
    public ResponseEntity<PagedResponse<MovieResponse>> getMoviesByDirector(@PathVariable(value = "slug") String slug,
                                                                            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByDirector(slug, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-type")
    public ResponseEntity<PagedResponse<MovieResponse>> getMoviesByType(@RequestParam(value = "type") String type,
                                                                        @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                        @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getMoviesByType(type, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-hot")
    public ResponseEntity<PagedResponse<MovieResponse>> getHotMovies(@RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                     @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iMovieService.getAllHotMovies(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }


}
