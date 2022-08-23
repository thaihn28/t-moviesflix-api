package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.PlaylistRepository;
import com.example.tmovierestapi.service.IPlaylistService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlaylistServiceImpl implements IPlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse<Playlist> getAllPlaylists(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<Playlist> playlists = playlistRepository.findAll(pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    public PlaylistDTO addPlaylist(PlaylistDTO playlistDTO) {
        // Convert DTO to Entity
        Playlist playlistRequest = modelMapper.map(playlistDTO, Playlist.class);
        Playlist playlist = playlistRepository.save(playlistRequest);

        // convert entity to DTO
        PlaylistDTO playlistResponse = modelMapper.map(playlist, PlaylistDTO.class);
        return playlistResponse;
    }

    @Override
    public PlaylistDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));
        return modelMapper.map(playlist, PlaylistDTO.class);
    }

    @Override
    public PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));

        // Convert DTO to entity
        Playlist playlistRequest = modelMapper.map(playlistDTO, Playlist.class);
        playlist.setName(playlistRequest.getName());
        playlist.setOriginName(playlistRequest.getOriginName());
        playlist.setSlug(playlistRequest.getSlug());
        playlist.setYear(playlistRequest.getYear());
        playlist.setThumbURL(playlistRequest.getThumbURL());
        playlistRepository.save(playlist);

        // Convert entity to DTO
        PlaylistDTO playlistDTOResponse = modelMapper.map(playlist, PlaylistDTO.class);
        return playlistDTOResponse;
    }

    @Override
    public void deletePlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));
         playlistRepository.delete(playlist);
    }

}
