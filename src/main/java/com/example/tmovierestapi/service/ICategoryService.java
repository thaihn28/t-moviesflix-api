package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface ICategoryService {
    PagedResponse<Category> getAllCategories(int pageNo, int pageSize, String sortDir, String sortBy);
    CategoryDTO addCategory(CategoryDTO categoryDTO);
}
