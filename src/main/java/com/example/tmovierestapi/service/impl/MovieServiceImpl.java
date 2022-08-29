package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.*;
import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import com.example.tmovierestapi.payload.request.EpisodeRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.*;
import com.example.tmovierestapi.service.IMovieService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements IMovieService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Override
    public PagedResponse<Movie> getAllMovies(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Movie> movies = movieRepository.findAll(pageable);
        List<Movie> data = movies.getNumberOfElements() == 0 ? Collections.emptyList() : movies.getContent();

        return new PagedResponse<>(data, movies.getNumber(), movies.getSize(), movies.getTotalElements(),
                movies.getTotalPages(), movies.isLast());
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        if(movieRepository.existsMovieByName(movieDTO.getName())){
            throw new APIException(HttpStatus.BAD_REQUEST, movieDTO.getName() + " is already exist!");
        }
        // Convert DTO to Entity
        Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

        Country country = countryRepository.findCountryById(movieDTO.getCountryID())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", movieDTO.getCountryID()));

        Set<Category> categorySet= new HashSet<>();
        Set<Director> directorSet= new HashSet<>();
        Set<Actor> actorSet= new HashSet<>();
        Set<Episode> episodeSet= new HashSet<>();

        for(CategoryRequest c : movieDTO.getCategories()){
            Category category = categoryRepository.findCategoryById(c.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", c.getId()));
            categorySet.add(category);
        }

        for(ActorRequest a : movieDTO.getActors()){
            Actor actor = actorRepository.findActorById(a.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", a.getId()));
            actorSet.add(actor);
        }

        for(DirectorRequest d : movieDTO.getDirectors()){
            Director director = directorRepository.findDirectorById(d.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", d.getId()));
            directorSet.add(director);
        }

        for(Episode e : movieDTO.getEpisodes()){
//            Episode episode = episodeRepository.findEpisodeById(e.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Episode", "ID", e.getId()));
//            episodeSet.add(episode);
            Episode episodeRequest = new Episode();
            episodeRequest.setId(e.getId());
            episodeRequest.setFilename(e.getFilename());
            episodeRequest.setLinkEmbed(e.getLinkEmbed());
            episodeRequest.setName(e.getName());
            episodeRequest.setCreatedDate(Instant.now());
            episodeRequest.setMovie(movieRequest);
//            episodeRepository.save(episodeRequest);
//            episodeSet.add(episodeRequest);
            movieRequest.addEpisode(episodeRequest);
        }

        movieRequest.setCountry(country);
        movieRequest.setCategories(categorySet);
        movieRequest.setCreatedDate(Instant.now());
        movieRequest.setActors(actorSet);
        movieRequest.setDirectors(directorSet);
//        movieRequest.setEpisodes(episodeSet);

        Movie movie = movieRepository.save(movieRequest);
        // Convert Entiry to DTO
        MovieDTO movieRespone = modelMapper.map(movie, MovieDTO.class);

        return movieRespone;
    }
}
