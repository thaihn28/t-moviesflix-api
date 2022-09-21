package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.dto.CountryDTO;
import com.example.tmovierestapi.service.ICountryService;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "country")
public class CountryController {
    @Autowired
    private ICountryService iCountryService;

    @Operation(description = "View all list countries", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Country.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries(){
        return new ResponseEntity<>(iCountryService.getAllCountries(), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CountryDTO> addCountry(@RequestBody @Valid CountryDTO countryDTO){
        return new ResponseEntity<>(iCountryService.addCountry(countryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CountryDTO> updateCountry(@PathVariable(value = "id") Long id, @RequestBody @Valid
                                                    CountryDTO countryDTO
                                                    ){
        return new ResponseEntity<>(iCountryService.updateCountry(id, countryDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCountry(@PathVariable(value = "id") Long id){
        iCountryService.deleteCountry(id);
        return new ResponseEntity<>("Deleted country with ID-" + id + " successfully", HttpStatus.OK);
    }

}
