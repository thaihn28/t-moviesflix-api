package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.*;
import com.example.tmovierestapi.payload.request.ActorRequest;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.request.DirectorRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.PaymentMovieResponse;
import com.example.tmovierestapi.repository.*;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.service.cloudinary.CloudinaryService;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    private final String MOVIE_HASH_KEY = "movie";

    @Override
    @Cacheable(value = "movies", key = "#pageSize")
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
    @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector"}
            , allEntries = true)
    @CachePut(value = MOVIE_HASH_KEY, key = "#result.id")
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile) {
        Boolean isExist = movieRepository.existsMovieBySlug(movieDTO.getSlug());
        if (isExist) {
            throw new APIException(HttpStatus.BAD_REQUEST, movieDTO.getSlug() + " slug is already exist!");
        }

        Boolean isSlugExistInPlaylist = playlistRepository.existsPlaylistBySlug(movieDTO.getSlug());
        if(!isSlugExistInPlaylist){
            throw new APIException(HttpStatus.BAD_REQUEST, movieDTO.getSlug() + " is not match with slug in Playlist!");
        }

        // Convert DTO to Entity
        Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

        Country country = countryRepository.findCountryBySlug(movieDTO.getCountrySlug())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "name", movieDTO.getCountrySlug()));

        Set<Category> categorySet = new HashSet<>();
        Set<Director> directorSet = new HashSet<>();
        Set<Actor> actorSet = new HashSet<>();

        for (CategoryRequest c : movieDTO.getCategories()) {
            Category category = categoryRepository.findCategoryById(c.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", c.getId()));
            categorySet.add(category);
        }

        for (ActorRequest a : movieDTO.getActors()) {
            Actor actor = actorRepository.findActorById(a.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", a.getId()));
            actorSet.add(actor);
        }

        for (DirectorRequest d : movieDTO.getDirectors()) {
            Director director = directorRepository.findDirectorById(d.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", d.getId()));
            directorSet.add(director);
        }

        if (movieDTO.getIsPremium()) {
            movieRequest.setPrice(0d);
        } else {
            movieRequest.setPrice(movieDTO.getPrice());
        }

        String thumbURL = cloudinaryService.uploadThumb(thumbFile);
        movieRequest.setThumbURL(thumbURL);

        String posterURL = cloudinaryService.uploadPoster(posterFile);
        movieRequest.setPosterURL(posterURL);

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
    @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector"}
            , allEntries = true)
    @CachePut(value = MOVIE_HASH_KEY, key = "#id")
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO, MultipartFile thumbFile, MultipartFile posterFile) {
        try {
            Movie movie = movieRepository.findMovieById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", id));
            //  DTO -> Entity
            Movie movieRequest = modelMapper.map(movieDTO, Movie.class);

            if (thumbFile != null) {
                movie.setThumbURL(cloudinaryService.uploadThumb(thumbFile));
            }
            if (posterFile != null) {
                movie.setPosterURL(cloudinaryService.uploadPoster(posterFile));
            }

            movie.setName(movieRequest.getName());
            movie.setContent(movieRequest.getContent());
            movie.setType(movieRequest.getType());
            movie.setTrailerURL(movieRequest.getTrailerURL());
            movie.setTime(movieRequest.getTime());
            movie.setEpisodeCurrent(movieRequest.getEpisodeCurrent());
            movie.setEpisodeTotal(movieRequest.getEpisodeTotal());
            movie.setQuality(movieRequest.getQuality());
            movie.setSlug(movieRequest.getSlug());
            movie.setYear(movieRequest.getYear());
            movie.setShowTimes(movieRequest.getShowTimes());
            movie.setIsPremium(movieRequest.getIsPremium());
            movie.setIsHot(movieRequest.getIsHot());
            movie.setPrice(movieRequest.getPrice());
            movie.setModifiedDate(LocalDateTime.now());

            Set<Category> categorySet = new HashSet<>();
            Set<Director> directorSet = new HashSet<>();
            Set<Actor> actorSet = new HashSet<>();

            Country country = countryRepository.findCountryBySlug(movieDTO.getCountrySlug())
                    .orElseThrow(() -> new ResourceNotFoundException("Country", "Name", movieDTO.getCountrySlug()));

            for (ActorRequest a : movieDTO.getActors()) {
                Actor actor = actorRepository.findActorById(a.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor", "ID", a.getId()));
                actorSet.add(actor);
            }
            for (CategoryRequest c : movieDTO.getCategories()) {
                Category category = categoryRepository.findCategoryById(c.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", c.getId()));
                categorySet.add(category);
            }

            for (DirectorRequest d : movieDTO.getDirectors()) {
                Director director = directorRepository.findDirectorById(d.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Director", "ID", d.getId()));
                directorSet.add(director);
            }
            movie.setCountry(country);
            movie.setCategories(categorySet);
            movie.setActors(actorSet);
            movie.setDirectors(directorSet);

            Movie convertToDTO = movieRepository.save(movie);
            MovieDTO movieResponse = modelMapper.map(convertToDTO, MovieDTO.class);

            return movieResponse;
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = MOVIE_HASH_KEY, key = "#id"),
            @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector"}
                    , allEntries = true)
    })
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", id));
        movieRepository.delete(movie);
    }

    @Override
    public PaymentMovieResponse getMovieBySlug(String slug) {
          /*TODO: If user is logged in
                    -> find Payment by User
                    -> if payment is founded
                        get Movie from Payment
                        convert Movie -> PaymentMovieResponse
                       else
                        return movie
                else
                    return movie
        */
        Movie movie = movieRepository.findMovieBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "Slug", slug));
        Object principal = AppGetLoggedIn.getLoggedIn().getPrincipal();
        String userName;

        PaymentMovieResponse movieResponse = new PaymentMovieResponse(
                movie.getId(),
                movie.getName(),
                movie.getOriginName(),
                movie.getContent(),
                movie.getType(),
                movie.getThumbURL(),
                movie.getTrailerURL(),
                movie.getTime(),
                movie.getEpisodeCurrent(),
                movie.getEpisodeTotal(),
                movie.getQuality(),
                movie.getSlug(),
                movie.getPosterURL(),
                movie.getYear(),
                movie.getShowTimes(),
                movie.getIsHot(),
                movie.getIsPremium(),
                movie.getPrice(),
                movie.getCreatedDate(),
                movie.getModifiedDate(),
                movie.getCountry(),
                movie.getActors(),
                movie.getDirectors(),
                movie.getCategories(),
                movie.getEpisodes(),
                movie.getComments()
        );

        if (principal instanceof CustomUserDetails) {
            userName = ((CustomUserDetails) principal).getUsername();
            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("Can not found user"));

            Optional<PaymentModel> paymentModel = paymentRepository.getPaymentModelByUserIdAndMovie(user.getId(), movie);
            if (paymentModel.isPresent()) {
                Movie movieInPaymentModel = paymentModel.get().getMovie();

                PaymentMovieResponse paymentMovieResponse = new PaymentMovieResponse(
                        movieInPaymentModel.getId(),
                        movieInPaymentModel.getName(),
                        movieInPaymentModel.getOriginName(),
                        movieInPaymentModel.getContent(),
                        movieInPaymentModel.getType(),
                        movieInPaymentModel.getThumbURL(),
                        movieInPaymentModel.getTrailerURL(),
                        movieInPaymentModel.getTime(),
                        movieInPaymentModel.getEpisodeCurrent(),
                        movieInPaymentModel.getEpisodeTotal(),
                        movieInPaymentModel.getQuality(),
                        movieInPaymentModel.getSlug(),
                        movieInPaymentModel.getPosterURL(),
                        movieInPaymentModel.getYear(),
                        movieInPaymentModel.getShowTimes(),
                        movieInPaymentModel.getIsHot(),
                        false,
                        0d,
                        movieInPaymentModel.getCreatedDate(),
                        movieInPaymentModel.getModifiedDate(),
                        movieInPaymentModel.getCountry(),
                        movieInPaymentModel.getActors(),
                        movieInPaymentModel.getDirectors(),
                        movieInPaymentModel.getCategories(),
                        movieInPaymentModel.getEpisodes(),
                        movieInPaymentModel.getComments()
                );
                return paymentMovieResponse;
            } else {
                return movieResponse;
            }
        } else {
            return movieResponse;
        }
    }

    @Override
    @Cacheable(value = "moviesByActor")
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
    @Cacheable(value = "moviesByDirector")
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

    @Override
    public PagedResponse<Movie> getAllHotMovies(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Boolean isHot = true;

        Page<Movie> movieResponse = movieRepository.findMoviesByIsHot(isHot, pageable);
        List<Movie> contents = movieResponse.getTotalElements() == 0 ? Collections.emptyList() : movieResponse.getContent();

        return new PagedResponse<>(contents, movieResponse.getNumber(), movieResponse.getSize(),
                movieResponse.getTotalElements(), movieResponse.getTotalPages(), movieResponse.isLast());
    }

}
