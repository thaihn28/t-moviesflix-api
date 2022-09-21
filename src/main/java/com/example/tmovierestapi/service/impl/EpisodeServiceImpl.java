package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Episode;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.EpisodeResponse;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.EpisodeRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.IEpisodeService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class EpisodeServiceImpl implements IEpisodeService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public PagedResponse<EpisodeResponse> getAllEpisodes(int pageNo, int pageSize, String sortDir, String sortBy) {

        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Episode> episodePage = episodeRepository.findAll(pageable);

        List<Episode> episodes = episodePage.getTotalElements() == 0 ? Collections.emptyList() : episodePage.getContent();

        List<EpisodeResponse> episodeResponses = new ArrayList<>();

        for (Episode e : episodes) {
            EpisodeResponse episodeResponseObj = new EpisodeResponse();
            episodeResponseObj.setId(e.getId());
            episodeResponseObj.setName(e.getName());
            episodeResponseObj.setFilename(e.getFilename());
            episodeResponseObj.setLinkEmbed(e.getLinkEmbed());
            episodeResponseObj.setCreatedDate(e.getCreatedDate());
            episodeResponseObj.setModifiedDate(e.getModifiedDate());

            Movie movieObj = e.getMovie();

            MovieResponse movieResponseObj = new MovieResponse();

            movieResponseObj.setId(movieObj.getId());
            movieResponseObj.setName(movieObj.getName());
            movieResponseObj.setOriginName(movieObj.getOriginName());
            movieResponseObj.setThumbURL(movieObj.getThumbURL());
            movieResponseObj.setYear(movieObj.getYear());
            movieResponseObj.setType(movieObj.getType());
            movieResponseObj.setSlug(movieObj.getSlug());

            episodeResponseObj.setMovie(movieResponseObj);

            episodeResponses.add(episodeResponseObj);
        }

        return new PagedResponse<>(episodeResponses, episodePage.getNumber(), episodePage.getSize()
                , episodePage.getTotalElements(), episodePage.getTotalPages(), episodePage.isLast()
        );
    }

    @Override
    public EpisodeDTO addEpisode(EpisodeDTO episodeDTO) {
        Boolean existFileName = episodeRepository.existsEpisodeByFilename(episodeDTO.getFilename());
        Boolean existLinkEmbed = episodeRepository.existsEpisodeByLinkEmbed(episodeDTO.getLinkEmbed());
        if (existFileName) {
            throw new APIException(
                    HttpStatus.BAD_REQUEST,
                    "File name: " +
                            episodeDTO.getFilename() + " is already exists");
        }
        if (existLinkEmbed) {
            throw new APIException(
                    HttpStatus.BAD_REQUEST,
                    "Link embed: " +
                            episodeDTO.getLinkEmbed() + " is already exists");
        }

        Movie movie = movieRepository.findMovieById(episodeDTO.getMovieID())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", episodeDTO.getMovieID()));
        // Convert DTO to Entity
        Episode episodeRequest = modelMapper.map(episodeDTO, Episode.class);
        episodeRequest.setName(episodeDTO.getName());
        episodeRequest.setFilename(episodeDTO.getFilename());
        episodeRequest.setLinkEmbed(episodeDTO.getLinkEmbed());
        episodeRequest.setMovie(movie);
        episodeRequest.setCreatedDate(LocalDateTime.now());

        Episode episodeResponse = episodeRepository.save(episodeRequest);
        // Convert Entity to DTO
        EpisodeDTO episodeResponseDTO = modelMapper.map(episodeResponse, EpisodeDTO.class);

        return episodeResponseDTO;
    }

    @Override
    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        Episode episode = episodeRepository.findEpisodeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "ID", id));

        Episode episodeRequest = modelMapper.map(episodeDTO, Episode.class);

        episode.setName(episodeRequest.getName());
        episode.setFilename(episodeRequest.getFilename());
        episode.setLinkEmbed(episodeRequest.getLinkEmbed());
        episode.setModifiedDate(LocalDateTime.now());

        Movie movie = movieRepository.findMovieById(episodeDTO.getMovieID())
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "ID", episodeDTO.getMovieID()));

        episode.setMovie(movie);

        Episode episodeResponse = episodeRepository.save(episode);
        EpisodeDTO episodeDTOResponse = modelMapper.map(episodeResponse, EpisodeDTO.class);

        return episodeDTOResponse;
    }

    @Override
    public void deleteByID(Long id) {
        Episode episode = episodeRepository.findEpisodeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "ID", id));
        episodeRepository.delete(episode);
    }
}
