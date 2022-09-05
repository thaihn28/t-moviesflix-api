package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.dto.CountryDTO;
import com.example.tmovierestapi.service.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {
    @Autowired
    private ICountryService iCountryService;

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries(){
        return new ResponseEntity<>(iCountryService.getAllCountries(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CountryDTO> addCountry(@RequestBody @Valid CountryDTO countryDTO){
        return new ResponseEntity<>(iCountryService.addCountry(countryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CountryDTO> updateCountry(@PathVariable(value = "id") Long id, @RequestBody @Valid
                                                    CountryDTO countryDTO
                                                    ){
        return new ResponseEntity<>(iCountryService.updateCountry(id, countryDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable(value = "id") Long id){
        iCountryService.deleteCountry(id);
        return new ResponseEntity<>("Deleted country with ID-" + id + " successfully", HttpStatus.OK);
    }

}
