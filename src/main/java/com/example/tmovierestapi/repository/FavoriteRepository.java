package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Favorite;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findFavoriteById(Long id);

    Optional<Favorite> findFavoriteByUser(User user);

    Page<Favorite> findFavoritesByUser(User user, Pageable pageable);

    Optional<Favorite> findFavoritesByPlaylists(Playlist playlist);
    Boolean existsFavoriteByPlaylists(Playlist playlist);
}
