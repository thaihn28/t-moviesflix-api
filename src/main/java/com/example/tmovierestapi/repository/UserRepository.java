package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
}
