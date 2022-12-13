package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.EpisodeResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IEpisodeService;
import com.example.tmovierestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/episodes")
@Tag(name = "episode")
public class EpisodeController {
    @Autowired
    private IEpisodeService iEpisodeService;

    @Operation(description = "View all list episodes", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Episode.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagedResponse<EpisodeResponse>> getAllEpisodes(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ){
        return new ResponseEntity<>(iEpisodeService.getAllEpisodes(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EpisodeDTO> addEpisode(@RequestBody @Valid EpisodeDTO episodeDTO){
        return new ResponseEntity<>(iEpisodeService.addEpisode(episodeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EpisodeDTO> updateEpisode(@PathVariable(value = "id") Long id, @RequestBody @Valid EpisodeDTO episodeDTO){
        return new ResponseEntity<>(iEpisodeService.updateEpisode(id, episodeDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteEpisode(@PathVariable(value = "id") Long id){
        iEpisodeService.deleteByID(id);
        return new ResponseEntity<>("Deleted Episode with ID-" + id + " successfully!", HttpStatus.OK);
    }
}
