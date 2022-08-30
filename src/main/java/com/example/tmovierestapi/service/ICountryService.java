package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.dto.CountryDTO;

import java.util.List;

public interface ICountryService {
    List<Country> getAllCountries();
    CountryDTO addCountry(CountryDTO countryDTO);
}
