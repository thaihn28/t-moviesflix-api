package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findDirectorById(Long id);
    Boolean existsDirectorByName(String name);
    //    @Query("SELECT a FROM Actor a WHERE a.name LIKE %?1%")
    @Query("SELECT d FROM Director d WHERE " +
            "d.name LIKE CONCAT('%', :keyword, '%')")
    Page<Director> searchDirectorsByKeyword(String keyword, Pageable pageable);
}
