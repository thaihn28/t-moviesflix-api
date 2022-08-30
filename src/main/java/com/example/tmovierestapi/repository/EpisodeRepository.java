package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Optional<Episode> findEpisodeById(Long id);
    Boolean existsEpisodeByFilename(String name);
}
