package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
    Optional<Movie> findMovieBySlug(String slug);
    Optional<Movie> findMovieById(Long id);
    Page<Movie> findMoviesByCategoriesIn(Set<Category> categories, Pageable pageable);
//    @Query("SELECT m FROM Movie m WHERE m.categories in :cates")
//    Page<Movie> findMoviesByCategories(@Param("cates") Set<Category> categories, Pageable pageable);
    Page<Movie> findMoviesByActors(Actor actor, Pageable pageable);
    Page<Movie> findMoviesByDirectors(Director director, Pageable pageable);
    Page<Movie> findMoviesByType(String type, Pageable pageable);
    Page<Movie> findMoviesByIsHot(Boolean isHot, Pageable pageable);
    Boolean existsMovieBySlug(String slug);
    //    @Query("SELECT a FROM Actor a WHERE a.name LIKE %?1%")
    @Query("SELECT m FROM Movie m WHERE " +
            "m.name LIKE CONCAT('%', :keyword, '%')"
            + "OR m.originName LIKE CONCAT('%', :keyword, '%')"
    )
    Page<Movie> searchMoviesByKeyword(String keyword, Pageable pageable);
}
