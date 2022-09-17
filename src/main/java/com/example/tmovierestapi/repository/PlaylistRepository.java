package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findById(Long id);

    Optional<Playlist> findPlaylistBySlug(String slug);
    Boolean existsPlaylistBySlug(String slug);
    Page<Playlist> findPlaylistsByIsHot(Boolean isHot, Pageable pageable);
    Page<Playlist> findPlaylistByType(String type, Pageable pageable);
    Page<Playlist> findPlaylistsByIsPremium(Boolean isPremium, Pageable pageable);
    Page<Playlist> findPlaylistByCategories(Category category, Pageable pageable);
    Page<Playlist> findPlaylistsByCountry(Country country, Pageable pageable);
}
