package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.stereotype.Service;

public interface IPlaylistService {
    PagedResponse<Playlist> getAllPlaylists(int pageNo, int pageSize, String sortDir, String sortBy);
    PlaylistDTO addPlaylist(PlaylistDTO playlistDTO);
    PlaylistDTO getPlaylistById(Long id);
    PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO);
    void deletePlaylistById(Long id);
}
