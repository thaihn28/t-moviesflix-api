package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Director;
import com.example.tmovierestapi.payload.dto.DirectorDTO;
import com.example.tmovierestapi.payload.response.DirectorResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IDirectorService {
    PagedResponse<DirectorResponse> getAllDirectors(int pageNo, int pageSize, String sortDir, String sortBy);
    DirectorDTO addDirector(DirectorDTO directorDTO, MultipartFile avatar);
    DirectorDTO updateDirector(Long id, DirectorDTO directorDTO, MultipartFile updateAvatar);
    void deleteDirector(Long id);
    Director getDirectorByID(Long id);
}
