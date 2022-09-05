package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.response.SearchResultsResponse;

public interface ISearchService {
    SearchResultsResponse searchByKeyword(String keyword, int pageNo, int pageSize);
}
