package com.example.tmovierestapi;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.repository.PlaylistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;


    @Test
    public void addMovie(){

//        Movie movie = movieRepository.findMovieBySlug("la-em");
//        Assertions.assertThat(movie.getSlug()).isEqualTo("la-em");
    }

}
