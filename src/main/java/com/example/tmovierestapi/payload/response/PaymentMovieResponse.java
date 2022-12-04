package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.model.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PaymentMovieResponse {
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 200, message = "Movie name must be minimum 2 characters and maximum 200 characters")
    private String name;

    @NotEmpty
    @Size(min = 2, max = 200, message = "Movie origin name must be minimum 2 characters and maximum 200 characters")
    private String originName;

    @NotEmpty(message = "Content should not be null or empty")
    private String content;

    @NotBlank
    private String type;

    private String thumbURL;

    @NotBlank
    private String trailerURL;

    @NotBlank
    private String time;

    @NotBlank
    private String quality;

    @NotBlank
    private String slug;

    private String posterURL;

    private Integer year;


    private Boolean isHot;

    private Boolean isPremium;

    private Double price;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    // Country
    private Country country;

    private Set<Actor> actors;

    private Set<Director> directors;

    private Set<Category> categories;

    private Set<Episode> episodes;

    private Set<Comment> comments;

    public PaymentMovieResponse(Long id, String name, String originName, String content, String type, String thumbURL, String trailerURL, String time, String quality, String slug, String posterURL, Integer year, Boolean isHot, Boolean isPremium, Double price, LocalDateTime createdDate, LocalDateTime modifiedDate, Country country, Set<Actor> actors, Set<Director> directors, Set<Category> categories, Set<Episode> episodes, Set<Comment> comments) {
        this.id = id;
        this.name = name;
        this.originName = originName;
        this.content = content;
        this.type = type;
        this.thumbURL = thumbURL;
        this.trailerURL = trailerURL;
        this.time = time;
        this.quality = quality;
        this.slug = slug;
        this.posterURL = posterURL;
        this.year = year;
        this.isHot = isHot;
        this.isPremium = isPremium;
        this.price = price;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.country = country;
        this.actors = actors;
        this.directors = directors;
        this.categories = categories;
        this.episodes = episodes;
        this.comments = comments;
    }
}
