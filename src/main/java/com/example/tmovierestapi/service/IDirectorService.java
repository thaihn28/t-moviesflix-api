package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface IDirectorService {
    PagedResponse<Director> getAllDirectors(int pageNo, int pageSize, String sortDir, String sortBy);
    DirectorDTO addDirector(DirectorDTO directorDTO);
}
