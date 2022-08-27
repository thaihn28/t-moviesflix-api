package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IPlaylistService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(iPlaylistService.getPlaylistById(id), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<PlaylistDTO> addPlaylist(@RequestBody @Valid PlaylistDTO playlistDTO) {
        return new ResponseEntity<>(iPlaylistService.addPlaylist(playlistDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable(value = "id") Long id,@RequestBody @Valid PlaylistDTO playlistDTO){
        return new ResponseEntity<>(iPlaylistService.updatePlaylist(id, playlistDTO), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlaylistByID(@PathVariable(value = "id") Long id){
        iPlaylistService.deletePlaylistById(id);
        return new ResponseEntity<>("Deleted playlist ID " + id + " successfully", HttpStatus.OK);
    }


}
