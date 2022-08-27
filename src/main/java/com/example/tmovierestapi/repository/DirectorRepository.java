package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findDirectorById(Long id);
    Boolean existsDirectorByName(String name);
}
