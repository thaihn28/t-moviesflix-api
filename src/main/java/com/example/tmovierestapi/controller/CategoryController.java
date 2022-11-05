package com.example.tmovierestapi.controller;

import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.CategoryResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.service.ICategoryService;
import com.example.tmovierestapi.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "category")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @Operation(description = "View all list categories", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Category.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "OK"),
            @ApiResponse(responseCode  = "401", description = "Unauthorized"),
            @ApiResponse(responseCode  = "403", description = "Forbidden"),
            @ApiResponse(responseCode  = "404", description = "Not found")
    })

    @GetMapping
    public ResponseEntity<PagedResponse<CategoryResponse>> getAllCategories(
            @RequestParam(value = "pageNo", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION) String sortDir
    ) {
        PagedResponse<CategoryResponse> response = iCategoryService.getAllCategories(pageNo, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return new ResponseEntity<>(iCategoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable(value = "id") Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        return new ResponseEntity<>(iCategoryService.updateCategory(id, categoryDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(value = "id") Long id) {
        iCategoryService.deleteCategory(id);
        return new ResponseEntity<>("Deleted category with ID-" + id + "successfully", HttpStatus.OK);
    }

}
