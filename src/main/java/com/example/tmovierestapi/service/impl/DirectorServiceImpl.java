package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.request.MovieRequest;
import com.example.tmovierestapi.payload.response.ActorResponse;
import com.example.tmovierestapi.payload.response.DirectorResponse;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.DirectorRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.IDirectorService;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class DirectorServiceImpl implements IDirectorService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final String DIRECTOR_HASH_KEY = "director";

    @Override
    @Cacheable(value = "directors", key = "{#pageSize, #pageNo, #sortDir, #sortBy}")
    public PagedResponse<DirectorResponse> getAllDirectors(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Director> directorsPage = directorRepository.findAll(pageable);

        List<Director> directors = directorsPage.getNumberOfElements() == 0 ? Collections.emptyList() : directorsPage.getContent();

        List<DirectorResponse> directorsResponse = new ArrayList<>();

        for(Director d : directors){
            DirectorResponse directorResponseObj = new DirectorResponse();
            directorResponseObj.setId(d.getId());
            directorResponseObj.setName(d.getName());
            directorResponseObj.setAvatar(d.getAvatar());
            directorResponseObj.setSlug(d.getSlug());
            directorResponseObj.setIsHot(d.getIsHot());
            directorResponseObj.setCreatedDate(d.getCreatedDate());
            directorResponseObj.setModifiedDate(d.getModifiedDate());

            Set<MovieResponse> movieResponseSet = new HashSet<>();
            for(Movie m : d.getMovies()){
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
            directorResponseObj.setMovies(movieResponseSet);
            directorsResponse.add(directorResponseObj);
        }


        return new PagedResponse<>(directorsResponse, directorsPage.getNumber(), directorsPage.getSize(), directorsPage.getTotalElements()
                ,directorsPage.getTotalPages(), directorsPage.isLast());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"directors", "moviesByDirector", "movies", "moviesByType", "hotMovies"},
                    allEntries = true)
    })
    @CachePut(value = DIRECTOR_HASH_KEY, key = "#result.id")
    public DirectorDTO addDirector(DirectorDTO directorDTO, MultipartFile avatar) {
        // Convert DTO to Entity
        Director directorRequest = modelMapper.map(directorDTO, Director.class);
        directorRequest.setCreatedDate(LocalDateTime.now());

//        Set<Movie> movieSet = new HashSet<>();
//
//        for(MovieRequest m : directorDTO.getMovies()){
//            Movie movie = movieRepository.findMovieById(m.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", m.getId()));
//            movieSet.add(movie);
//        }
//        directorRequest.setMovies(movieSet);

        String avatarURL = cloudinaryService.uploadThumb(avatar);
        directorRequest.setAvatar(avatarURL);
        Director director = directorRepository.save(directorRequest);

        DirectorDTO directorResponse = modelMapper.map(director, DirectorDTO.class);
        return directorResponse;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"directors", "moviesByDirector", "movies", "moviesByType", "hotMovies"},
                    allEntries = true)
    })
    @CachePut(value = DIRECTOR_HASH_KEY, key = "#result.id")
    public DirectorDTO addDirector(DirectorDTO directorDTO) {
        // Convert DTO to Entity
        Director directorRequest = modelMapper.map(directorDTO, Director.class);
        directorRequest.setCreatedDate(LocalDateTime.now());

//        Set<Movie> movieSet = new HashSet<>();
//
//        for(MovieRequest m : directorDTO.getMovies()){
//            Movie movie = movieRepository.findMovieById(m.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", m.getId()));
//            movieSet.add(movie);
//        }
//        directorRequest.setMovies(movieSet);

        directorRequest.setAvatar(directorDTO.getAvatar());
        Director director = directorRepository.save(directorRequest);

        DirectorDTO directorResponse = modelMapper.map(director, DirectorDTO.class);
        return directorResponse;
    }

    @Override
    @CacheEvict(value = {"directors", "moviesByDirector", "movies", "moviesByType", "hotMovies"}
            , allEntries = true)
    @CachePut(value = DIRECTOR_HASH_KEY, key = "#id")
    public DirectorDTO updateDirector(Long id, DirectorDTO directorDTO, MultipartFile updateAvatar) {
        Director director = directorRepository.findDirectorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", id));

        Director directorRequest = modelMapper.map(directorDTO, Director.class);

        if(!updateAvatar.isEmpty()){
            String url = cloudinaryService.uploadThumb(updateAvatar);
            director.setAvatar(url);
        }
        director.setName(directorRequest.getName());
        director.setSlug(directorRequest.getSlug());
        director.setIsHot(directorRequest.getIsHot());
        director.setModifiedDate(LocalDateTime.now());

        Director directorResponse = directorRepository.save(director);
        DirectorDTO directorDTOResponse = modelMapper.map(directorResponse, DirectorDTO.class);

        return directorDTOResponse;
    }

    @Override
    @CacheEvict(value = {"directors", "moviesByDirector", "movies", "moviesByType", "hotMovies"}
            , allEntries = true)
    @CachePut(value = DIRECTOR_HASH_KEY, key = "#id")
    public DirectorDTO updateDirector(Long id, DirectorDTO directorDTO) {
        Director director = directorRepository.findDirectorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", id));

        Director directorRequest = modelMapper.map(directorDTO, Director.class);

        director.setAvatar(directorRequest.getAvatar());
        director.setName(directorRequest.getName());
        director.setSlug(directorRequest.getSlug());
        director.setIsHot(directorRequest.getIsHot());
        director.setModifiedDate(LocalDateTime.now());

        Director directorResponse = directorRepository.save(director);
        DirectorDTO directorDTOResponse = modelMapper.map(directorResponse, DirectorDTO.class);

        return directorDTOResponse;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = DIRECTOR_HASH_KEY, key = "#id"),
            @CacheEvict(value = {"directors", "moviesByDirector", "movies", "moviesByType", "hotMovies"}
                    , allEntries = true)
    })
    public void deleteDirector(Long id) {
        Director director = directorRepository.findDirectorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", id));
        for (Movie m : director.getMovies()){
            m.removeDirector(director);
        }
        directorRepository.delete(director);
    }

    @Override
    @Cacheable(value = DIRECTOR_HASH_KEY, key = "#id")
    public Director getDirectorByID(Long id) {
        Director director = directorRepository.findDirectorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", id));
        return director;
    }
}
