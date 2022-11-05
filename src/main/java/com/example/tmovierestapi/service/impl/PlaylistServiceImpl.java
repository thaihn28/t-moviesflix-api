package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.*;
import com.example.tmovierestapi.payload.dto.PlaylistDTO;
import com.example.tmovierestapi.payload.request.CategoryRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CategoryRepository;
import com.example.tmovierestapi.repository.CountryRepository;
import com.example.tmovierestapi.repository.PlaylistRepository;
import com.example.tmovierestapi.service.IFavoriteService;
import com.example.tmovierestapi.service.IPlaylistService;
import com.example.tmovierestapi.service.cloudinary.CloudinaryService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PlaylistServiceImpl implements IPlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final String PLAYLIST_HASH_KEY = "playlist";

    @Autowired
    private IFavoriteService iFavoriteService;


    @Override
    @Cacheable(value = "playlists", key = "{#pageSize, #pageNo, #sortDir, #sortBy}")
    public PagedResponse<Playlist> getAllPlaylists(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<Playlist> playlists = playlistRepository.findAll(pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    @CacheEvict(value = {"playlists", "hotPlaylists", "playlistsBySeries", "playlistsByCate", "playlistsByCountry", "premiumPlaylists"}
            , allEntries = true)
    @CachePut(value = PLAYLIST_HASH_KEY, key = "#result.id")
    public PlaylistDTO addPlaylist(PlaylistDTO playlistDTO, MultipartFile thumbFile, MultipartFile posterFile) {
        Boolean isExist = playlistRepository.existsPlaylistBySlug(playlistDTO.getSlug());
        if (isExist) {
            throw new APIException(HttpStatus.BAD_REQUEST, playlistDTO.getSlug() + " is already exist");
        }
        // Convert DTO to Entity
        Playlist playlistRequest = modelMapper.map(playlistDTO, Playlist.class);
        if (!thumbFile.isEmpty()) {
            playlistRequest.setThumbURL(cloudinaryService.uploadThumb(thumbFile));
        }
        if(!posterFile.isEmpty()){
            playlistRequest.setPosterURL(cloudinaryService.uploadPoster(posterFile));
        }
        Set<Category> categorySet = new HashSet<>();

        for (CategoryRequest c : playlistDTO.getCategories()) {
            Category category = categoryRepository.findCategoryById(c.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", c.getId()));
            categorySet.add(category);
        }
        Country country = countryRepository.findCountryBySlug(playlistDTO.getCountrySlug())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "Name", playlistDTO.getCountrySlug()));

        playlistRequest.setCreatedDate(LocalDateTime.now());
        playlistRequest.setCategories(categorySet);
        playlistRequest.setCountry(country);

        Playlist playlist = playlistRepository.save(playlistRequest);

        // convert entity to DTO
        PlaylistDTO playlistResponse = modelMapper.map(playlist, PlaylistDTO.class);
        return playlistResponse;
    }

    @Override
    @Cacheable(value = PLAYLIST_HASH_KEY, key = "#id")
    public PlaylistDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));
        return modelMapper.map(playlist, PlaylistDTO.class);
    }

    @Override
    @CacheEvict(value = {"playlists", "hotPlaylists", "playlistsBySeries", "playlistsByCate", "playlistsByCountry", "premiumPlaylists"},
            allEntries = true)
    @CachePut(value = PLAYLIST_HASH_KEY, key = "#id")
    public PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO, MultipartFile thumbFile) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));

        // Convert DTO to entity
        Playlist playlistRequest = modelMapper.map(playlistDTO, Playlist.class);

        if (thumbFile != null) {
            playlist.setThumbURL(cloudinaryService.uploadThumb(thumbFile));
        }

        playlist.setName(playlistRequest.getName());
        playlist.setOriginName(playlistRequest.getOriginName());
        playlist.setType(playlistRequest.getType());
        playlist.setEpisodeCurrent(playlistRequest.getEpisodeCurrent());
        playlist.setTime(playlistRequest.getTime());
        playlist.setQuality(playlistRequest.getQuality());
        playlist.setSlug(playlistRequest.getSlug());
        playlist.setYear(playlistRequest.getYear());
        playlist.setIsHot(playlistRequest.getIsHot());
        playlist.setIsPremium(playlistRequest.getIsPremium());

        Set<Category> categorySet = new HashSet<>();

        for (CategoryRequest c : playlistDTO.getCategories()) {
            Category category = categoryRepository.findCategoryById(c.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", c.getId()));
            categorySet.add(category);
        }
        Country country = countryRepository.findCountryBySlug(playlistDTO.getCountrySlug())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "Name", playlistDTO.getCountrySlug()));
        playlist.setCategories(categorySet);
        playlist.setCountry(country);
        playlist.setModifiedDate(LocalDateTime.now());

        playlistRepository.save(playlist);

        // Convert entity to DTO
        PlaylistDTO playlistDTOResponse = modelMapper.map(playlist, PlaylistDTO.class);
        return playlistDTOResponse;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = PLAYLIST_HASH_KEY, key = "#id"),
            @CacheEvict(value = {"playlists", "hotPlaylists", "playlistsBySeries", "playlistsByCate", "playlistsByCountry", "premiumPlaylists"},
                    allEntries = true)
    })
    public void deletePlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", id));
        iFavoriteService.deletePlaylistInFavorite(id);

        playlistRepository.delete(playlist);
    }

    @Override
    @Cacheable(value = "hotPlaylists")
    public PagedResponse<Playlist> getAllHotPlaylists(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<Playlist> playlists = playlistRepository.findPlaylistsByIsHot(true, pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    @Cacheable(value = "playlistsBySeries")
    public PagedResponse<Playlist> getAllPlaylistsBySeries(String series, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<Playlist> playlists = playlistRepository.findPlaylistByType(series, pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    @Cacheable(value = "playlistsByCate")
    public PagedResponse<Playlist> getAllPlaylistByCategory(String slug, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Category category = categoryRepository.findCategoryBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Name", slug));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Playlist> playlists = playlistRepository.findPlaylistByCategories(category, pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    @Cacheable(value = "playlistsByCountry")
    public PagedResponse<Playlist> getAllPlaylistByCountry(String slug, int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Country country = countryRepository.findCountryBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "Name", slug));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Playlist> playlists = playlistRepository.findPlaylistsByCountry(country, pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

    @Override
    @Cacheable(value = "premiumPlaylists")
    public PagedResponse<Playlist> getAllPremiumPlaylist(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sortOj = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortOj);
        Page<Playlist> playlists = playlistRepository.findPlaylistsByIsPremium(true, pageable);

        List<Playlist> items = playlists.getNumberOfElements() == 0 ? Collections.emptyList() : playlists.getContent();

        return new PagedResponse<>(items, playlists.getNumber(), playlists.getSize(), playlists.getTotalElements(),
                playlists.getTotalPages(), playlists.isLast());
    }

}
