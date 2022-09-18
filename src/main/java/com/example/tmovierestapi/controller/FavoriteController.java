package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Favorite;
import com.example.tmovierestapi.payload.dto.FavoriteDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IFavoriteService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    @Autowired
    private IFavoriteService iFavoriteService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<PagedResponse<Favorite>> getAllFavorites(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ){
        return new ResponseEntity<>(iFavoriteService.getAllFavoritesByUser(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<FavoriteDTO> addPlaylistToFav(@RequestParam(name = "slug") String slug){
        return new ResponseEntity<>(iFavoriteService.addFavorite(slug), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> deletePlaylistInFav(@RequestParam(name = "playlist-id") Long id){
        return new ResponseEntity<>(iFavoriteService.deletePlaylistInFavorite(id), HttpStatus.OK);
    }
}
