package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Country;
import com.example.tmovierestapi.payload.dto.CountryDTO;
import com.example.tmovierestapi.repository.CountryRepository;
import com.example.tmovierestapi.service.ICountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(noRollbackFor = {ValidationException.class, APIException.class})
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
    @CacheEvict(value = {"playlists", "movies", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists",
            "moviesByActor", "moviesByDirector", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public CountryDTO addCountry(CountryDTO countryDTO) {
        Boolean existCountry = countryRepository.existsCountryByName(countryDTO.getName());
        if (existCountry) {
            throw new APIException(
                    HttpStatus.BAD_REQUEST,
                    countryDTO.getName() + " is already exists"
            );
        }
        Country countryRequest = modelMapper.map(countryDTO, Country.class);
        countryRequest.setName(countryDTO.getName());
        countryRequest.setCreatedDate(LocalDateTime.now());

        Country country = countryRepository.save(countryRequest);
        CountryDTO countryResponse = modelMapper.map(country, CountryDTO.class);

        return countryResponse;
    }

    @Override
    @CacheEvict(value = {"playlists", "movies", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists",
            "moviesByActor", "moviesByDirector", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public CountryDTO updateCountry(Long id, CountryDTO countryDTO) {
        Country country = countryRepository.findCountryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "ID", id));

        Country countryRequest = modelMapper.map(countryDTO, Country.class);
        country.setName(countryRequest.getName());
        country.setModifiedDate(LocalDateTime.now());

        Country countryResponse = countryRepository.save(country);
        CountryDTO countryDTOResponse = modelMapper.map(countryResponse, CountryDTO.class);

        return countryDTOResponse;
    }

    @Override
    @CacheEvict(value = {"playlists", "movies", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists",
            "moviesByActor", "moviesByDirector", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public void deleteCountry(Long id) {
        Country country = countryRepository.findCountryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "ID", id));
        countryRepository.delete(country);
    }
}
