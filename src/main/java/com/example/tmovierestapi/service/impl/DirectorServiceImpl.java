package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.request.MovieRequest;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.DirectorRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.IDirectorService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DirectorServiceImpl implements IDirectorService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Override
    public PagedResponse<Director> getAllDirectors(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Director> directors = directorRepository.findAll(pageable);

        List<Director> directorResponse = directors.getNumberOfElements() == 0 ? Collections.emptyList() : directors.getContent();
        return new PagedResponse<>(directorResponse, directors.getNumber(), directors.getSize(), directors.getTotalElements() ,directors.getTotalPages(), directors.isLast());
    }

    @Override
    public DirectorDTO addDirector(DirectorDTO directorDTO) {
        if(directorRepository.existsDirectorByName(directorDTO.getName())){
            throw new APIException(HttpStatus.BAD_REQUEST, directorDTO.getName() + " is already exist");
        }
        // Convert DTO to Entity
        Director directorRequest = modelMapper.map(directorDTO, Director.class);
        directorRequest.setCreatedDate(Instant.now());

        Set<Movie> movieSet = new HashSet<>();

        for(MovieRequest m : directorDTO.getMovies()){
            Movie movie = movieRepository.findMovieById(m.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "ID", m.getId()));
            movieSet.add(movie);
        }
        directorRequest.setMovies(movieSet);

        Director director = directorRepository.save(directorRequest);

        DirectorDTO directorResponse = modelMapper.map(director, DirectorDTO.class);
        return directorResponse;
    }
}
