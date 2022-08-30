package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.dto.CountryDTO;
import com.example.tmovierestapi.repository.CountryRepository;
import com.example.tmovierestapi.service.ICountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CountryServiceImpl implements ICountryService {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public CountryDTO addCountry(CountryDTO countryDTO) {
        Boolean existCountry = countryRepository.existsCountryByName(countryDTO.getName());
        if(existCountry){
            throw new APIException(
                    HttpStatus.BAD_REQUEST,
                    countryDTO.getName() + " is already exists"
            );
        }
        Country countryRequest = modelMapper.map(countryDTO, Country.class);
        countryRequest.setName(countryDTO.getName());
        countryRequest.setCreatedDate(Instant.now());

        Country country = countryRepository.save(countryRequest);
        CountryDTO countryResponse = modelMapper.map(country, CountryDTO.class);

        return countryResponse;
    }
}
