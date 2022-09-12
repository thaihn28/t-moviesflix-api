package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.anotation.ValidImage;
import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.response.DirectorResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.IDirectorService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/directors")
public class DirectorController {

    @Autowired
    private IDirectorService iDirectorService;

    @GetMapping
    public ResponseEntity<PagedResponse<DirectorResponse>> getAllDirectors(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        return new ResponseEntity<>(iDirectorService.getAllDirectors(pageNo, pageSize, sortDir, sortBy), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Director> getDirectorByID(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(iDirectorService.getDirectorByID(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DirectorDTO> addDirector(@ModelAttribute(name = "director") @Valid DirectorDTO directorDTO,
                                                   @RequestPart(name = "avatarFile") @Valid @ValidImage MultipartFile avatar) {
        return new ResponseEntity<>(iDirectorService.addDirector(directorDTO, avatar), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DirectorDTO> updateDirector(@PathVariable(value = "id") Long id,
                                                      @ModelAttribute @Valid DirectorDTO directorDTO,
                                                      @RequestPart(name = "avatarFile", required = false) @ValidImage MultipartFile updateAvatar
    ) {
        return new ResponseEntity<>(iDirectorService.updateDirector(id, directorDTO, updateAvatar), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteDirector(@PathVariable(value = "id") Long id) {
        iDirectorService.deleteDirector(id);
        return new ResponseEntity<>("Deleted director with ID-" + id + "successfully", HttpStatus.OK);
    }

}
