package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findActorById(Long id);
    Boolean existsActorByName(String name);
}
