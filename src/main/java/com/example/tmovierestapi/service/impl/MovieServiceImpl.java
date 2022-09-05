package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.*;
import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.*;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.service.cloudinary.CloudinaryService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private CloudinaryService cloudinaryService;


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
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) {
        Boolean isExist = movieRepository.existsMovieBySlug(movieDTO.getSlug());
        if(isExist){
            throw new APIException(HttpStatus.BAD_REQUEST, movieDTO.getSlug() + " slug is already exist!");
        }
        // Convert DTO to Entity
        Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

        Country country = countryRepository.findCountryByName(movieDTO.getCountryName())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", movieDTO.getCountryName()));

        Set<Category> categorySet= new HashSet<>();
        Set<Director> directorSet= new HashSet<>();
        Set<Actor> actorSet= new HashSet<>();

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

        if (movieDTO.getIsFree()) {
            movieRequest.setPrice(0);
        } else {
            movieRequest.setPrice(movieDTO.getPrice());
        }

//        String url = cloudinaryService.uploadFile(file);
//        movieRequest.setThumbURL(file);

        movieRequest.setCountry(country);
        movieRequest.setCategories(categorySet);
        movieRequest.setCreatedDate(LocalDateTime.now());
        movieRequest.setActors(actorSet);
        movieRequest.setDirectors(directorSet);

        Movie movie = movieRepository.save(movieRequest);
        // Convert Entiry to DTO
        MovieDTO movieRespone = modelMapper.map(movie, MovieDTO.class);

        return movieRespone;
    }

    @Override
    public PagedResponse<Movie> getMoviesByCategory(Long categoryID, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Category category = categoryRepository.findCategoryById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryID));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Movie> movieResponse = movieRepository.findMoviesByCategories(category, pageable);

        List<Movie> contents = movieResponse.getTotalElements() == 0 ? Collections.emptyList() : movieResponse.getContent();

        return new PagedResponse<>(contents, movieResponse.getNumber(), movieResponse.getSize(),
                movieResponse.getTotalElements(), movieResponse.getTotalPages(), movieResponse.isLast());
    }

    @Override
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", id));
        //  DTO -> Entity
        Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

        movie.setName(movieRequest.getName());
        movie.setContent(movieRequest.getContent());
        movie.setType(movieRequest.getType());
        movie.setThumbURL(movieRequest.getThumbURL());
        movie.setTrailerURL(movieRequest.getTrailerURL());
        movie.setTime(movieRequest.getTime());
        movie.setEpisodeCurrent(movieRequest.getEpisodeCurrent());
        movie.setEpisodeTotal(movieRequest.getEpisodeTotal());
        movie.setQuality(movieRequest.getQuality());
        movie.setSlug(movieRequest.getSlug());
        movie.setYear(movieRequest.getYear());
        movie.setPosterURL(movieRequest.getPosterURL());
        movie.setShowTimes(movieRequest.getShowTimes());
        movie.setIsFree(movieRequest.getIsFree());
        movie.setIsHot(movieRequest.getIsHot());
        movie.setModifiedDate(LocalDateTime.now());

        Set<Category> categorySet= new HashSet<>();
        Set<Director> directorSet= new HashSet<>();
        Set<Actor> actorSet= new HashSet<>();

        Country country = countryRepository.findCountryByName(movieDTO.getCountryName())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "Name", movieDTO.getCountryName()));

        for(ActorRequest a : movieDTO.getActors()){
            Actor actor = actorRepository.findActorById(a.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", a.getId()));
            actorSet.add(actor);
        }
        for(CategoryRequest c : movieDTO.getCategories()){
            Category category = categoryRepository.findCategoryById(c.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", c.getId()));
            categorySet.add(category);
        }

        for(DirectorRequest d : movieDTO.getDirectors()){
            Director director = directorRepository.findDirectorById(d.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", d.getId()));
            directorSet.add(director);
        }
        movie.setCountry(country);
        movie.setCategories(categorySet);
        movie.setActors(actorSet);
        movie.setDirectors(directorSet);

        Movie convertToDTO =  movieRepository.save(movie);
        MovieDTO movieResponse = modelMapper.map(convertToDTO, MovieDTO.class);

        return movieResponse;
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", id));
        movieRepository.delete(movie);
    }

    @Override
    public Movie getMovieBySlug(String slug) {
        Movie movie = movieRepository.findMovieBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "Slug", slug));
        return movie;
    }

    @Override
    public PagedResponse<Movie> getMoviesByActor(Long actorID, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Actor actor = actorRepository.findActorById(actorID)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", actorID));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Movie> movieResponse = movieRepository.findMoviesByActors(actor, pageable);

        List<Movie> contents = movieResponse.getTotalElements() == 0 ? Collections.emptyList() : movieResponse.getContent();

        return new PagedResponse<>(contents, movieResponse.getNumber(), movieResponse.getSize(),
                movieResponse.getTotalElements(), movieResponse.getTotalPages(), movieResponse.isLast());
    }

    @Override
    public PagedResponse<Movie> getMoviesByDirector(Long directorID, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Director director = directorRepository.findDirectorById(directorID)
                .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", directorID));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Movie> movieResponse = movieRepository.findMoviesByDirectors(director, pageable);

        List<Movie> contents = movieResponse.getTotalElements() == 0 ? Collections.emptyList() : movieResponse.getContent();

        return new PagedResponse<>(contents, movieResponse.getNumber(), movieResponse.getSize(),
                movieResponse.getTotalElements(), movieResponse.getTotalPages(), movieResponse.isLast());
    }

    @Override
    public PagedResponse<Movie> getMoviesByType(String type, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Movie> movieResponse = movieRepository.findMoviesByType(type, pageable);
        List<Movie> contents = movieResponse.getTotalElements() == 0 ? Collections.emptyList() : movieResponse.getContent();

        return new PagedResponse<>(contents, movieResponse.getNumber(), movieResponse.getSize(),
                movieResponse.getTotalElements(), movieResponse.getTotalPages(), movieResponse.isLast());
    }

}
