package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.request.MovieRequest;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CategoryRepository;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.service.ICategoryService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse<Category> getAllCategories(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);

        List<Category> categoryResponse = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();
        return new PagedResponse<>(categoryResponse, categories.getNumber(), categories.getSize(), categories.getTotalElements(), categories.getTotalPages(), categories.isLast());
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsCategoryByName(categoryDTO.getName())){
            throw new APIException(HttpStatus.BAD_REQUEST, categoryDTO.getName() + " is already exist");
        }
        // Convert DTO to Entity
        Category categoryRequest = modelMapper.map(categoryDTO, Category.class);
        Set<Movie> movieSet = new HashSet<>();


        for(MovieRequest m : categoryDTO.getMovies()){
            Movie movie = movieRepository.findMovieById(m.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", m.getId()));
            movieSet.add(movie);
        }
        categoryRequest.setMovies(movieSet);
        categoryRequest.setCreatedDate(Instant.now());
        Category category = categoryRepository.save(categoryRequest);

        // Convert Entity to DTO
        CategoryDTO categoryResponse = modelMapper.map(category, CategoryDTO.class);

        return categoryResponse;
    }
}
