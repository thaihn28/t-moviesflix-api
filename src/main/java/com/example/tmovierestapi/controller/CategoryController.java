package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.CategoryResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CategoryRepository;
import com.example.tmovierestapi.service.ICategoryService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<PagedResponse<CategoryResponse>> getAllCategories(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_DATE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<CategoryResponse> response = iCategoryService.getAllCategories(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return new ResponseEntity<>(iCategoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteMovie(@PathVariable(value = "id") Long id){
//        Category test = categoryRepository.findCategoryById(id)
//                .orElseThrow(() -> new IllegalStateException("safd"));
//        System.out.println(test.getMovies());
//        for(Movie movie : test.getMovies()){
//            movie.removeCategory(test);
//        }
//        categoryRepository.deleteById(id);
//
//        return new ResponseEntity<>("Delted", HttpStatus.OK);
//    }

}
