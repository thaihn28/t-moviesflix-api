package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Favorite;
import com.example.tmovierestapi.payload.dto.FavoriteDTO;
import com.example.tmovierestapi.service.IFavoriteService;
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

    @PostMapping("/add")
//    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Favorite> addPlaylistToFav(@RequestBody @Valid FavoriteDTO favoriteDTO){
        return new ResponseEntity<>(iFavoriteService.addFavorite(favoriteDTO), HttpStatus.CREATED);
    }
}
