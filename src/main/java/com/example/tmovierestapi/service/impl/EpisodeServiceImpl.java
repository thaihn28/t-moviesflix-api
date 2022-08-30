package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.EpisodeRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.IEpisodeService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class EpisodeServiceImpl implements IEpisodeService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public PagedResponse<Episode> getAllEpisodes(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Episode> episodePage = episodeRepository.findAll(pageable);
        List<Episode> episodesResponse = episodePage.getTotalElements() == 0 ? Collections.emptyList() : episodePage.getContent();

        return new PagedResponse<>(episodesResponse, episodePage.getNumber(), episodePage.getSize(),
                episodePage.getTotalElements(), episodePage.getTotalPages(), episodePage.isLast());
    }

    @Override
    public EpisodeDTO addEpisode(EpisodeDTO episodeDTO) {
        Boolean existEpisode = episodeRepository.existsEpisodeByFilename(episodeDTO.getFilename());
        if(existEpisode){
            throw new APIException(
                    HttpStatus.BAD_REQUEST,
                    episodeDTO.getFilename() + "is already exists");
        }

        Movie movie = movieRepository.findMovieById(episodeDTO.getMovieID())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", episodeDTO.getMovieID()));
        // Convert DTO to Entity
        Episode episodeRequest = modelMapper.map(episodeDTO, Episode.class);
        episodeRequest.setName(episodeDTO.getName());
        episodeRequest.setFilename(episodeDTO.getFilename());
        episodeRequest.setLinkEmbed(episodeDTO.getLinkEmbed());
        episodeRequest.setMovie(movie);
        episodeRequest.setCreatedDate(Instant.now());

        Episode episodeResponse = episodeRepository.save(episodeRequest);
        // Convert Entity to DTO
        EpisodeDTO episodeResponseDTO = modelMapper.map(episodeResponse, EpisodeDTO.class);

        return episodeResponseDTO;
    }
}
