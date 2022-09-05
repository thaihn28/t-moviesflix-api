package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.payload.dto.EpisodeDTO;
import com.example.tmovierestapi.payload.response.EpisodeResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IEpisodeService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/episodes")
public class EpisodeController {
    @Autowired
    private IEpisodeService iEpisodeService;

    @GetMapping
    public ResponseEntity<PagedResponse<EpisodeResponse>> getAllEpisodes(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ){
        return new ResponseEntity<>(iEpisodeService.getAllEpisodes(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<EpisodeDTO> addEpisode(@RequestBody @Valid EpisodeDTO episodeDTO){
        return new ResponseEntity<>(iEpisodeService.addEpisode(episodeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EpisodeDTO> updateEpisode(@PathVariable(value = "id") Long id, @RequestBody @Valid EpisodeDTO episodeDTO){
        return new ResponseEntity<>(iEpisodeService.updateEpisode(id, episodeDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEpisode(@PathVariable(value = "id") Long id){
        iEpisodeService.deleteByID(id);
        return new ResponseEntity<>("Deleted Episode with ID-" + id + " successfully!", HttpStatus.OK);
    }
}
