package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface IEpisodeService {
    PagedResponse<Episode> getAllEpisodes(int pageNo, int pageSize, String sortDir, String sortBy);
    EpisodeDTO addEpisode(EpisodeDTO episodeDTO);
}
