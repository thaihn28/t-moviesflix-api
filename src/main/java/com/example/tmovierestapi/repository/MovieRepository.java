package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findMovieBySlug(String slug);
    Optional<Movie> findMovieById(Long id);
    Boolean existsMovieByName(String name);
    List<Movie> findMoviesByCategories(CategoryDTO categoryDTO, Pageable pageable);
}
