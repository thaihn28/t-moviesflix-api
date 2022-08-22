package com.example.tmovierestapi;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Test
    public void addMovie(){
        Playlist playlist = playlistRepository.getPlaylistById(Long.valueOf(1));
        System.out.println(playlist.getSlug() + "asdsag");
        Movie movie = movieRepository.findMovieByPlaylistSlug("la-em");
        System.out.println(movie.getSlug());

    }
}
