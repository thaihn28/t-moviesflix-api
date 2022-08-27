package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IDirectorService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/directors")
public class DirectorController {

    @Autowired
    private IDirectorService iDirectorService;

    @GetMapping
    public ResponseEntity<PagedResponse<Director>> getAllDirectors(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_DATE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ){
        return new ResponseEntity<>(iDirectorService.getAllDirectors(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<DirectorDTO> addDirector(@RequestBody @Valid DirectorDTO directorDTO){
        return new ResponseEntity<>(iDirectorService.addDirector(directorDTO), HttpStatus.CREATED);
    }

}
