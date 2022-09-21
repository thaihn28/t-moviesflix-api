package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.response.*;
import com.example.tmovierestapi.repository.ActorRepository;
import com.example.tmovierestapi.repository.DirectorRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.ISearchService;
import com.example.tmovierestapi.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Override
    public SearchResultsResponse searchByKeyword(String keyword, int pageNo, int pageSize) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        SearchResultsResponse resultsResponse = new SearchResultsResponse();

        Pageable pageable = PageRequest.of(pageNo, pageSize);

//        String newStr = keyword.trim()
//                .replaceAll("[ ]+", ""); //multiple spaces to single space

        String newStr = keyword.trim();
        if(newStr.isEmpty() || newStr == null){
            throw new APIException(HttpStatus.NO_CONTENT, "No data");
        }

        Page<Movie> moviesPage = movieRepository.searchMoviesByKeyword(newStr, pageable);
        Page<Actor> actorsPage = actorRepository.searchActorsByKeyword(newStr, pageable);
        Page<Director> directorsPage = directorRepository.searchDirectorsByKeyword(newStr, pageable);

        Set<SearchMovieResponse> movieResponseSet = new HashSet<>();
        Set<SearchActorResponse> searchActorResponseSet = new HashSet<>();
        Set<SearchDirectorResponse> searchDirectorResponseSet = new HashSet<>();

        if (moviesPage.getTotalElements() > 0) {
            for (Movie m : moviesPage.getContent()) {
                SearchMovieResponse searchMovieResponse = new SearchMovieResponse();
                searchMovieResponse.setId(m.getId());
                searchMovieResponse.setName(m.getName());
                searchMovieResponse.setSlug(m.getSlug());
                searchMovieResponse.setYear(m.getYear());
                searchMovieResponse.setOriginName(m.getOriginName());
                searchMovieResponse.setThumbURL(m.getThumbURL());
                searchMovieResponse.setPosterURL(m.getPosterURL());

                movieResponseSet.add(searchMovieResponse);
            }
            resultsResponse.setMovies(movieResponseSet);
        }
        if (actorsPage.getTotalElements() > 0) {
            for (Actor a : actorsPage.getContent()) {
                SearchActorResponse searchActorResponse = new SearchActorResponse();
                searchActorResponse.setId(a.getId());
                searchActorResponse.setName(a.getName());
                searchActorResponse.setSlug(a.getSlug());
                searchActorResponse.setAvatar(a.getAvatar());

                searchActorResponseSet.add(searchActorResponse);
            }
            resultsResponse.setActors(searchActorResponseSet);
        }
        if (directorsPage.getTotalElements() > 0) {
            for (Director d : directorsPage.getContent()) {
                SearchDirectorResponse searchDirectorResponse = new SearchDirectorResponse();
                searchDirectorResponse.setId(d.getId());
                searchDirectorResponse.setName(d.getName());
                searchDirectorResponse.setSlug(d.getSlug());
                searchDirectorResponse.setAvatar(d.getAvatar());

                searchDirectorResponseSet.add(searchDirectorResponse);
            }
            resultsResponse.setDirectors(searchDirectorResponseSet);
        }
        return resultsResponse;
    }
}
