package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Favorite;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.model.User;
import com.example.tmovierestapi.payload.dto.FavoriteDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.FavoriteRepository;
import com.example.tmovierestapi.repository.PlaylistRepository;
import com.example.tmovierestapi.repository.UserRepository;
import com.example.tmovierestapi.security.services.CustomUserDetails;
import com.example.tmovierestapi.service.IFavoriteService;
import com.example.tmovierestapi.utils.AppGetLoggedIn;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse getAllFavoritesByUser(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Favorite> favorites;
        List<Favorite> data;

        Authentication authentication = AppGetLoggedIn.getLoggedIn();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(customUserDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(customUserDetails.getUsername() + " can not found"));

            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            favorites = favoriteRepository.findFavoritesByUser(currentUser, pageable);
            data = favorites.getNumberOfElements() == 0 ? Collections.emptyList() : favorites.getContent();
        } else {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Require user authentication");
        }

        return new PagedResponse<>(data, favorites.getNumber(), favorites.getSize(), favorites.getTotalElements(),
                favorites.getTotalPages(), favorites.isLast());
    }

    @Override
    public Favorite addFavorite(String slug) {
        Playlist playlist = playlistRepository.findPlaylistBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "Slug", slug));

        Favorite favoriteRequest = new Favorite();
        Set<Playlist> playlistSet = new HashSet<>();
        Favorite favoriteResponse = null;

        Authentication authentication = AppGetLoggedIn.getLoggedIn();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(customUserDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(customUserDetails.getUsername() + " can not found"));

            Optional<Favorite> checkFavByUser = favoriteRepository.findFavoriteByUser(currentUser);

            if (checkFavByUser.isPresent()) {
                Favorite favOfCurrentUser = checkFavByUser.get();
                if (favOfCurrentUser.getPlaylists() != null && favOfCurrentUser.getPlaylists().size() > 0) {
                    for (Playlist p : favOfCurrentUser.getPlaylists()) {
                        String existSlug = p.getSlug();
                        if (existSlug.equalsIgnoreCase(slug)) {
                            throw new APIException(HttpStatus.BAD_REQUEST,"Movie " + playlist.getName() + " is already exist in favorite!");
                        }
                    }
                    return updateFavorite(favOfCurrentUser, playlist);
                }
            }
            playlistSet.add(playlist);
            favoriteRequest.setUser(currentUser);
            favoriteRequest.setPlaylists(playlistSet);
            favoriteRequest.setCreatedDate(LocalDateTime.now());
            favoriteResponse = favoriteRepository.save(favoriteRequest);
        }
        return favoriteResponse;
    }

    private Favorite updateFavorite(Favorite favorite, Playlist playlist) {
        Favorite updateFav = favorite;
        Set<Playlist> playlistSet = updateFav.getPlaylists();
        playlistSet.add(playlist);
        updateFav.setPlaylists(playlistSet);
        updateFav.setModifiedDate(LocalDateTime.now());
        Favorite updatePlaylistInFav = favoriteRepository.save(updateFav);
        return updatePlaylistInFav;
    }

    @Override
    public void deletePlaylistInFavorite(Long playlistID) {
        Playlist playlist = playlistRepository.findById(playlistID)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "ID", playlistID));
        Favorite favorite = favoriteRepository.findFavoritesByPlaylists(playlist)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "Playlist", playlist.getName()));
        favorite.removePlaylist(playlist);
        favoriteRepository.save(favorite);
    }
}
