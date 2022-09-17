package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findCountryById(Long id);
    Optional<Country> findCountryBySlug(String slug);
    Boolean existsCountryByName(String name);

}
