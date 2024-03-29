package com.example.tmovierestapi.service.impl;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.exception.ResourceNotFoundException;
import com.example.tmovierestapi.model.Category;
import com.example.tmovierestapi.model.Movie;
import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.payload.dto.CategoryDTO;
import com.example.tmovierestapi.payload.response.CategoryResponse;
import com.example.tmovierestapi.payload.response.MovieResponse;
import com.example.tmovierestapi.payload.response.MovieResponseInOtherModel;
import com.example.tmovierestapi.payload.response.PagedResponse;
import com.example.tmovierestapi.repository.CategoryRepository;
import com.example.tmovierestapi.service.ICategoryService;
import com.example.tmovierestapi.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse<CategoryResponse> getAllCategories(int pageNo, int pageSize, String sortDir, String sortBy) {
        AppUtils.validatePageNumberAndSize(pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getNumberOfElements() == 0 ? Collections.emptyList() : categoryPage.getContent();

        List<CategoryResponse> categoriesDTOResponse = new ArrayList<>();
        for (Category c : categories) {
            CategoryResponse categoryResponseObj = new CategoryResponse();
            categoryResponseObj.setId(c.getId());
            categoryResponseObj.setName(c.getName());
            categoryResponseObj.setSlug(c.getSlug());
            categoryResponseObj.setCreatedDate(c.getCreatedDate());
            categoryResponseObj.setModifiedDate(c.getModifiedDate());
            Set<MovieResponseInOtherModel> movieResponseSet = new HashSet<>();
            for (Movie m : c.getMovies()) {
                MovieResponseInOtherModel movieResponseObj = new MovieResponseInOtherModel();
                movieResponseObj.setId(m.getId());
                movieResponseObj.setImdbID(m.getImdbID());
                movieResponseObj.setName(m.getName());
//                movieResponseObj.setYear(m.getYear());
//                movieResponseObj.setOriginName(m.getOriginName());
//                movieResponseObj.setThumbURL(m.getThumbURL());
//                movieResponseObj.setPosterURL(m.getPosterURL());
//                movieResponseObj.setTrailerURL(m.getTrailerURL());
//                movieResponseObj.setType(m.getType());
//                movieResponseObj.setSlug(m.getSlug());
//                movieResponseObj.setIsHot(m.getIsHot());
//                movieResponseObj.setIsPremium(m.getIsPremium());
//                movieResponseObj.setPrice(m.getPrice());

                movieResponseSet.add(movieResponseObj);
            }
            categoryResponseObj.setMovies(movieResponseSet);
            categoriesDTOResponse.add(categoryResponseObj);
        }

        return new PagedResponse<>(categoriesDTOResponse, categoryPage.getNumber(), categoryPage.getSize(), categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.isLast());
    }

    @Override
    @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector",
            "playlists", "hotPlaylists", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsCategoryByName(categoryDTO.getName())) {
            throw new APIException(HttpStatus.BAD_REQUEST, categoryDTO.getName() + " is already exist");
        }
        // Convert DTO to Entity
        Category categoryRequest = modelMapper.map(categoryDTO, Category.class);
//        Set<Movie> movieSet = new HashSet<>();


//        for(MovieRequest m : categoryDTO.getMovies()){
//            Movie movie = movieRepository.findMovieById(m.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", m.getId()));
//            movieSet.add(movie);
//        }
//        categoryRequest.setMovies(movieSet);

        categoryRequest.setCreatedDate(LocalDateTime.now());
        Category category = categoryRepository.save(categoryRequest);

        // Convert Entity to DTO
        CategoryDTO categoryResponse = modelMapper.map(category, CategoryDTO.class);

        return categoryResponse;
    }

    @Override
    @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector",
            "playlists", "hotPlaylists", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));

        Category categoryRequest = modelMapper.map(categoryDTO, Category.class);

        category.setName(categoryRequest.getName());
        category.setSlug(categoryRequest.getSlug());
        category.setModifiedDate(LocalDateTime.now());

        Category categoryResponse = categoryRepository.save(category);
        CategoryDTO categoryDTOResponse = modelMapper.map(categoryResponse, CategoryDTO.class);

        return categoryDTOResponse;
    }

    @Override
    @CacheEvict(value = {"movies", "moviesByActor", "moviesByDirector",
            "playlists", "hotPlaylists", "playlistsBySeries",
            "playlistsByCate", "playlistsByCountry", "premiumPlaylists", "moviesByType", "hotMovies"
    }
            , allEntries = true)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
        for (Movie movie : category.getMovies()) {
            movie.removeCategory(category);
        }
        for (Playlist playlist : category.getPlaylists()) {
            playlist.removeCategory(category);
        }
        categoryRepository.delete(category);
    }
}
