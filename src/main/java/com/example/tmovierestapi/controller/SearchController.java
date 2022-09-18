package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.payload.response.SearchResultsResponse;
import com.example.tmovierestapi.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    @Autowired
    private ISearchService iSearchService;

    @GetMapping()
    public ResponseEntity<SearchResultsResponse> searchByKeyword(
            @RequestParam(value = "keyword") @Valid String keyword,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize
    ){
       return new ResponseEntity<>(iSearchService.searchByKeyword(keyword, pageNo, pageSize), HttpStatus.OK);
    }

}
