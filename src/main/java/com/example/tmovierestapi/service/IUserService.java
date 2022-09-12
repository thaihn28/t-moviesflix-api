package com.example.tmovierestapi.service;

import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.payload.response.UserResponse;

public interface IUserService {
    PagedResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortDir, String sortBy);
    void deleteUserByID(Long id) throws Exception;
    UserResponse getUserByID(Long id);
    /* TODO: forget password*/
}
