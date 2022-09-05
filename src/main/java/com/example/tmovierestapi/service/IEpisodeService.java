package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.EpisodeResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;

import java.util.List;

public interface IEpisodeService {
    PagedResponse<EpisodeResponse> getAllEpisodes(int pageNo, int pageSize, String sortDir, String sortBy);
    EpisodeDTO addEpisode(EpisodeDTO episodeDTO);
    EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO);
    void deleteByID(Long id);
}
