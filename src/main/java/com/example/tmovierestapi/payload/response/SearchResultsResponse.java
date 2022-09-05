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
    Set<SearchMovieResponse> movies = new HashSet<>();
    Set<SearchActorResponse> actors = new HashSet<>();
    Set<SearchDirectorResponse> directors = new HashSet<>();
}
