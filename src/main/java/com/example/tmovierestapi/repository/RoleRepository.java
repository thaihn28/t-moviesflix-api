package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.enums.ERole;
import com.example.tmovierestapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(ERole name);
    Boolean existsRoleByName(ERole name);
}
