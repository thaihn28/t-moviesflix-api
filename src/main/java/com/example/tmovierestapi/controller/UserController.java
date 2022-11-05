package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.UserResponse;
import com.example.tmovierestapi.service.IUserService;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @Operation(description = "View all list users", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Playlist.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "username", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<UserResponse> pagedResponse = iUserService.getAllUsers(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<UserResponse> getUserByID(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(iUserService.getUserByID(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUserByID(@PathVariable(name = "id") Long id) throws Exception{
        iUserService.deleteUserByID(id);
        return new ResponseEntity<>("Deleted User with ID-" + id + " successfully!", HttpStatus.OK);
    }

}
