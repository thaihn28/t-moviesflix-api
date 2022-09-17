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
    public PagedResponse getAllFavorite(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Favorite> favorites = favoriteRepository.findAll(pageable);
        List<Favorite> data = favorites.getNumberOfElements() == 0 ? Collections.emptyList() : favorites.getContent();

        return new PagedResponse<>(data, favorites.getNumber(), favorites.getSize(), favorites.getTotalElements(),
                favorites.getTotalPages(), favorites.isLast());
    }

    @Override
    public Favorite addFavorite(FavoriteDTO favoriteDTO) {
        Playlist playlist = playlistRepository.findPlaylistBySlug(favoriteDTO.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "Slug", favoriteDTO.getSlug()));
        Boolean isExistPlaylist = favoriteRepository.existsFavoriteByPlaylists(playlist);

        Favorite favoriteRequest = modelMapper.map(favoriteDTO, Favorite.class);
        Set<Playlist> playlistSet = new HashSet<>();
        playlistSet.add(playlist);

        Authentication authentication = AppGetLoggedIn.getLoggedIn();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(customUserDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(customUserDetails.getUsername() + " can not found"));

            Optional<Favorite> checkFavByUser = favoriteRepository.findFavoriteByUser(currentUser);

            if(checkFavByUser.isPresent()){
                Favorite favOfCurrentUser = checkFavByUser.get();
                for(Playlist p : favOfCurrentUser.getPlaylists()){
                    if(p.getSlug().equals(favoriteDTO.getSlug())){
                        throw new APIException(HttpStatus.BAD_REQUEST, playlist.getName() + " is already exist in favorite!");
                    }else{
                        updateFavorite(favOfCurrentUser, playlist);
                    }
                }
            }else {
                favoriteRequest.setUser(currentUser);
                favoriteRequest.setPlaylists(playlistSet);
                favoriteRequest.setCreatedDate(LocalDateTime.now());
                Favorite favorite = favoriteRepository.save(favoriteRequest);
                return favorite;
            }
        }
        return null;
    }

    private Favorite updateFavorite(Favorite favorite, Playlist playlist){
        Favorite updateFav = favorite;
        Set<Playlist> playlistSet = new HashSet<>();
        playlistSet.add(playlist);
        updateFav.setPlaylists(playlistSet);
        updateFav.setModifiedDate(LocalDateTime.now());
        Favorite updatePlaylistInFav = favoriteRepository.save(updateFav);
        return updatePlaylistInFav;
    }

    @Override
    public void deleteFavorite(Long id) {

    }
}
