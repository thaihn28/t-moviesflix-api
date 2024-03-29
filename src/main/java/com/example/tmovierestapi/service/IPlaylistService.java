package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface IPlaylistService {
    PagedResponse<Playlist> getAllPlaylists(int pageNo, int pageSize, String sortDir, String sortBy);
    PlaylistDTO addPlaylist(PlaylistDTO playlistDTO, MultipartFile thumbFile, MultipartFile posterFile);
    PlaylistDTO getPlaylistById(Long id);
    PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO, MultipartFile thumbFile);
    void deletePlaylistById(Long id);
    PagedResponse<Playlist> getAllHotPlaylists(int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Playlist> getAllPlaylistsBySeries(String series ,int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Playlist> getAllPlaylistByCategory(String cateName ,int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Playlist> getAllPlaylistByCountry(String country,int pageNo, int pageSize, String sortDir, String sortBy);
    PagedResponse<Playlist> getAllPremiumPlaylist(int pageNo, int pageSize, String sortDir, String sortBy);
}
