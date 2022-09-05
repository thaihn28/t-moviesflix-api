package com.example.tmovierestapi;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.MovieDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.repository.PlaylistRepository;
import com.example.tmovierestapi.service.IMovieService;
import com.example.tmovierestapi.utils.AppConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback()
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private IMovieService iMovieService;


    @Test
    public void addMovie(){

        Movie movie = movieRepository.findMovieBySlug("la-em")
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "slug", "la-em"))
                ;
        Assertions.assertThat(movie.getSlug()).isEqualTo("la-em");

        // Add movie
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setName("La em");
        movieDTO.setContent("Content");
        movieDTO.setType("Type");
        movieDTO.setThumbURL("Thumb");
        movieDTO.setTrailerURL("trailer");
        movieDTO.setTime("Time");
        movieDTO.setEpisodeCurrent("Tap hien tai");
        movieDTO.setEpisodeTotal("Tong tap");
        movieDTO.setQuality("HD");
        movieDTO.setSlug("la-em");
        movieDTO.setPosterURL("Poster");

        Set<Long> categories = new HashSet<>();
        categories.add(Long.valueOf(1));
        categories.add(Long.valueOf(2));
        categories.add(Long.valueOf(3));
        categories.add(Long.valueOf(4));

//        iMovieService.addMovie(movieDTO);

        PagedResponse<Movie> movieList = iMovieService.getAllMovies(0, 5, "ASC", "name");
        Assertions.assertThat(movieList.getTotalElements()).isNotZero();

    }

}
