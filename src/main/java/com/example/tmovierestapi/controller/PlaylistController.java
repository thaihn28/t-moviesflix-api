package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IPlaylistService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {
    @Autowired
    private IPlaylistService iPlaylistService;

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

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlaylistDTO> addPlaylist(@ModelAttribute(name = "playlist") @Valid PlaylistDTO playlistDTO,
                                                   @RequestPart(name = "thumbFile") @ValidImage MultipartFile thumbFile
    ) {
        return new ResponseEntity<>(iPlaylistService.addPlaylist(playlistDTO, thumbFile), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable(value = "id") Long id,
                                                      @ModelAttribute(name = "playlist") @Valid PlaylistDTO playlistDTO,
                                                      @RequestPart(name = "thumbFile", required = false) @ValidImage MultipartFile thumbFile
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
