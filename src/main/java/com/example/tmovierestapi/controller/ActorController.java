package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.ActorResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IActorService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {
    @Autowired
    private IActorService iActorService;

    @GetMapping
    public ResponseEntity<PagedResponse<ActorResponse>> getAllActors(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir) {
        return new ResponseEntity<>(iActorService.getAllActors(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Actor> getActorByID(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(iActorService.getActorByID(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ActorDTO> addActor(@ModelAttribute(name = "actor") @Valid ActorDTO actorDTO,
                                             @RequestPart(name = "avatarFile") @ValidImage MultipartFile avatar) {
        return new ResponseEntity<>(iActorService.addActor(actorDTO, avatar), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable(value = "id") Long id, @RequestBody @Valid ActorDTO actorDTO) {
        return new ResponseEntity<>(iActorService.updateActor(id, actorDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable(value = "id") Long id) {
        iActorService.deleteActor(id);
        return new ResponseEntity<>("Deleted actor with ID-" + id + " successfully", HttpStatus.OK);
    }

}
