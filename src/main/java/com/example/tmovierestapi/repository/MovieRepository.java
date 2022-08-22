package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByPlaylistSlug(String slug);

}
