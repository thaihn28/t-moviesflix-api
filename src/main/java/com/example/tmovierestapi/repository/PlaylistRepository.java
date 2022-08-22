package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist getPlaylistById(Long id);
}
