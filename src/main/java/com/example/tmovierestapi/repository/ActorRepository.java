package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findActorById(Long id);
    Optional<Actor> findActorBySlug(String slug);
    Boolean existsActorBySlug(String slug);
    Optional<Actor> findActorsBySlug(String slug);

//    @Query("SELECT a FROM Actor a WHERE a.name LIKE %?1%")
    @Query("SELECT a FROM Actor a WHERE " +
            "a.name LIKE CONCAT('%', :keyword, '%')")
    Page<Actor> searchActorsByKeyword(String keyword, Pageable pageable);
}
