package com.example.tmovierestapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultsResponse {
    private Set<SearchMovieResponse> movies = new HashSet<>();
    private Set<SearchActorResponse> actors = new HashSet<>();
    private Set<SearchDirectorResponse> directors = new HashSet<>();
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
