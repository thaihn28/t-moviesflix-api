package com.example.tmovierestapi.service.csv;

import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.repository.MovieRepository;
import com.example.tmovierestapi.utils.AppCSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    @Autowired private MovieRepository movieRepository;

    public void save(MultipartFile file){
        try {
            List<Movie> movies = AppCSVHelper.csvToMovies(file.getInputStream());
            movieRepository.saveAll(movies);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
