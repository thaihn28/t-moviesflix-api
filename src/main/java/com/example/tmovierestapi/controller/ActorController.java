package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.ActorResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IActorService;
import com.example.tmovierestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/actors")
@Tag(name = "actor")
public class ActorController {
    @Autowired
    private IActorService iActorService;

    @Operation(description = "View all list actors", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Actor.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

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
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActorDTO> addActor(@RequestPart(name = "actor") @Valid ActorDTO actorDTO,
                                             @RequestPart(name = "avatar") @ValidImage MultipartFile avatar) {
        return new ResponseEntity<>(iActorService.addActor(actorDTO, avatar), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable(value = "id") Long id,
                                                @RequestPart(name = "actor") @Valid ActorDTO actorDTO,
                                                @RequestPart(name = "avatar", required = false) @ValidImage MultipartFile avatar
    ) {
        return new ResponseEntity<>(iActorService.updateActor(id, actorDTO, avatar), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteActor(@PathVariable(value = "id") Long id) {
        iActorService.deleteActor(id);
        return new ResponseEntity<>("Deleted actor with ID-" + id + " successfully", HttpStatus.OK);
    }

}
