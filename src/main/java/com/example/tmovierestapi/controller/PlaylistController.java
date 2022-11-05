package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IPlaylistService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/playlists")
@Tag(name = "playlist")
public class PlaylistController {
    @Autowired
    private IPlaylistService iPlaylistService;

    @Operation(description = "View all list playlists", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Playlist.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

    @GetMapping
    public ResponseEntity<PagedResponse<Playlist>> getAllPlaylists(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_YEAR, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<Playlist> response = iPlaylistService.getAllPlaylists(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(iPlaylistService.getPlaylistById(id), HttpStatus.OK);
    }

    @GetMapping("/filter-by-category/{slug}")
    public ResponseEntity<PagedResponse<Playlist>> getPlaylistsByCategory(@PathVariable(value = "slug") String slug,
                                                                          @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                          @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iPlaylistService.getAllPlaylistByCategory(slug, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-type/{name}")
    public ResponseEntity<PagedResponse<Playlist>> getPlaylistsByType(@PathVariable(value = "name") String type,
                                                                      @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                      @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iPlaylistService.getAllPlaylistsBySeries(type, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/filter-by-country/{slug}")
    public ResponseEntity<PagedResponse<Playlist>> getPlaylistsByCountry(@PathVariable(value = "slug") String slug,
                                                                      @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                      @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iPlaylistService.getAllPlaylistByCountry(slug, pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/hot-movie")
    public ResponseEntity<PagedResponse<Playlist>> getAllHotMovies(@RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iPlaylistService.getAllHotPlaylists(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/premium-movie")
    public ResponseEntity<PagedResponse<Playlist>> getAllPremiumMovies(@RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iPlaylistService.getAllPremiumPlaylist(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlaylistDTO> addPlaylist(@RequestPart(name = "playlist") @Valid PlaylistDTO playlistDTO,
                                                   @RequestPart(name = "thumb") @ValidImage MultipartFile thumbFile,
                                                   @RequestPart(name = "poster") @ValidImage MultipartFile posterFile
    ) throws IOException {
        if(thumbFile == null){
            throw new FileNotFoundException("Thumb file is required");
        }
        if(posterFile == null){
            throw new FileNotFoundException("Poster file is required");
        }
        return new ResponseEntity<>(iPlaylistService.addPlaylist(playlistDTO, thumbFile, posterFile), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable(value = "id") Long id,
                                                      @RequestPart(name = "playlist") @Valid PlaylistDTO playlistDTO,
                                                      @RequestPart(name = "thumb", required = false) @ValidImage MultipartFile thumbFile
    ) {
        return new ResponseEntity<>(iPlaylistService.updatePlaylist(id, playlistDTO, thumbFile), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePlaylistByID(@PathVariable(value = "id") Long id) {
        iPlaylistService.deletePlaylistById(id);
        return new ResponseEntity<>("Deleted playlist ID " + id + " successfully", HttpStatus.OK);
    }


}
