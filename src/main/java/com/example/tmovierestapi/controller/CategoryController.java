package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.ICategoryService;
import com.example.tmovierestapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping
    public ResponseEntity<PagedResponse<Category>> getAllCategories(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_CREATED_DATE, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<Category> response = iCategoryService.getAllCategories(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(iCategoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

}
