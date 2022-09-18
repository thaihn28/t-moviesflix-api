package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.dto.FavoriteDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface IFavoriteService {
    PagedResponse getAllFavoritesByUser(int pageNo, int pageSize, String sortDir, String sortBy);
    FavoriteDTO addFavorite(String slug);
    String deletePlaylistInFavorite(Long playlistID);
}
