package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Favorite;
import com.example.tmovierestapi.payload.dto.FavoriteDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface IFavoriteService {
    PagedResponse getAllFavorite(int pageNo, int pageSize, String sortDir, String sortBy);
    Favorite addFavorite(FavoriteDTO favoriteDTO);
    void deleteFavorite(Long id);
}
