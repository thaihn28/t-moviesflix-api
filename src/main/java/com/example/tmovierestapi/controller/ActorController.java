package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IActorService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {
    @Autowired
    private IActorService iActorService;

    @GetMapping
    public ResponseEntity<PagedResponse<Actor>> getAllActors(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_DATE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir)
    {
        return new ResponseEntity<>(iActorService.getAllActors(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ActorDTO> addActor(@RequestBody @Valid  ActorDTO actorDTO){
        return new ResponseEntity<>(iActorService.addActor(actorDTO), HttpStatus.CREATED);
    }

}
