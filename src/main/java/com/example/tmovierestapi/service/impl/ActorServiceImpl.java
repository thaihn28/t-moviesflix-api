package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.ActorResponse;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.ActorRepository;
import com.example.tmovierestapi.service.IActorService;
import com.example.tmovierestapi.service.cloudinary.CloudinaryService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ActorServiceImpl implements IActorService {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final String ACTOR_HASH_KEY = "actor";


    @Override
    @Cacheable(value = "actors", key = "{#pageSize, #pageNo, #sortDir, #sortBy}")
    public PagedResponse<ActorResponse> getAllActors(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Actor> actorPage = actorRepository.findAll(pageable);

        List<Actor> actors = actorPage.getNumberOfElements() == 0 ? Collections.emptyList() : actorPage.getContent();

        List<ActorResponse> actorsResponse = new ArrayList<>();

        for (Actor a : actors) {
            ActorResponse actorResponseObj = new ActorResponse();
            actorResponseObj.setId(a.getId());
            actorResponseObj.setName(a.getName());
            actorResponseObj.setSlug(a.getSlug());
            actorResponseObj.setAvatar(a.getAvatar());
            actorResponseObj.setIsHot(a.getIsHot());
            actorResponseObj.setCreatedDate(a.getCreatedDate());
            actorResponseObj.setModifiedDate(a.getModifiedDate());

            Set<MovieResponse> movieResponseSet = new HashSet<>();
            for (Movie m : a.getMovies()) {
                MovieResponse movieResponseObj = new MovieResponse();
                movieResponseObj.setId(m.getId());
                movieResponseObj.setName(m.getName());
                movieResponseObj.setThumbURL(m.getThumbURL());
                movieResponseObj.setYear(m.getYear());
                movieResponseObj.setOriginName(m.getOriginName());
                movieResponseObj.setType(m.getType());
                movieResponseObj.setSlug(m.getSlug());

                movieResponseSet.add(movieResponseObj);
            }
            actorResponseObj.setMovies(movieResponseSet);
            actorsResponse.add(actorResponseObj);
        }

        return new PagedResponse<>(actorsResponse, actorPage.getNumber(), actorPage.getSize(),
                actorPage.getTotalElements(), actorPage.getTotalPages(), actorPage.isLast());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"actors", "moviesByActor", "movies"},
                    allEntries = true)
    })
    @CachePut(value = ACTOR_HASH_KEY, key = "#result.id")
    public ActorDTO addActor(ActorDTO actorDTO, MultipartFile avatar) {
        // Convert DTO to Entity
        Actor actor = modelMapper.map(actorDTO, Actor.class);

//        Set<Movie> movieSet = new HashSet<>();

//        for(MovieRequest m : actorDTO.getMovies()){
//            Movie movie = movieRepository.findMovieById(m.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", m.getId()));
//            movieSet.add(movie);
//        }
        //        actor.setMovies(movieSet);
        String avatarURL = cloudinaryService.uploadThumb(avatar);
        actor.setAvatar(avatarURL);
        actor.setCreatedDate(LocalDateTime.now());
        Actor actorRequest = actorRepository.save(actor);
        // Convert Entity to DTO
        ActorDTO actorResponse = modelMapper.map(actorRequest, ActorDTO.class);

        return actorResponse;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"actors", "moviesByActor", "movies"},
                    allEntries = true)
    })
    @CachePut(value = ACTOR_HASH_KEY, key = "#id")
    public ActorDTO updateActor(Long id, ActorDTO actorDTO, MultipartFile avatar) {
        Actor actor = actorRepository.findActorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", id));

        Actor actorRequest = modelMapper.map(actorDTO, Actor.class);

        if (avatar.isEmpty()) {
            String url = cloudinaryService.uploadThumb(avatar);
            actor.setAvatar(url);
        }
        actor.setName(actorRequest.getName());
        actor.setSlug(actorRequest.getSlug());
        actor.setIsHot(actorRequest.getIsHot());
        actor.setModifiedDate(LocalDateTime.now());

        Actor actorResponse = actorRepository.save(actor);
        ActorDTO actorDTOResponse = modelMapper.map(actorResponse, ActorDTO.class);

        return actorDTOResponse;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = ACTOR_HASH_KEY, key = "#id"),
            @CacheEvict(value = {"actors", "moviesByActor", "movies"},
                    allEntries = true)
    })
    public void deleteActor(Long id) {
        Actor actor = actorRepository.findActorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", id));
        for (Movie m : actor.getMovies()) {
            m.removeActor(actor);
        }
        actorRepository.delete(actor);
    }

    @Override
    @Cacheable(value = ACTOR_HASH_KEY, key = "#id")
    public Actor getActorByID(Long id) {
        Actor actor = actorRepository.findActorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", id));
        return actor;
    }
}
