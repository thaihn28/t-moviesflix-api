package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.request.MovieRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.ActorRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.IActorService;
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
public class ActorServiceImpl implements IActorService {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public PagedResponse<Actor> getAllActors(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Actor> actors = actorRepository.findAll(pageable);

        List<Actor> actorResponse =  actors.getNumberOfElements() == 0 ? Collections.emptyList() : actors.getContent();

        return new PagedResponse<>(actorResponse, actors.getNumber(), actors.getSize(),
                actors.getTotalElements(), actors.getTotalPages(), actors.isLast());
    }

    @Override
    public ActorDTO addActor(ActorDTO actorDTO) {
        // Convert DTO to Entity
        Actor actor = modelMapper.map(actorDTO, Actor.class);

        actor.setCreatedDate(Instant.now());
        Set<Movie> movieSet = new HashSet<>();

        for(MovieRequest m : actorDTO.getMovies()){
            Movie movie = movieRepository.findMovieById(m.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", m.getId()));
            movieSet.add(movie);
        }
        actor.setMovies(movieSet);
        Actor actorRequest = actorRepository.save(actor);
        // Convert Entity to DTO
        ActorDTO actorResponse = modelMapper.map(actorRequest, ActorDTO.class);

        return actorResponse;
    }
}
