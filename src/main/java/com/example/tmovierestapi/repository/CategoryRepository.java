package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryById(Long id);
    Optional<Category> findCategoryBySlug(String slug);
    Boolean existsCategoryBySlug(String slug);
    Boolean existsCategoryByName(String name);
}
