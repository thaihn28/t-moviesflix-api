package com.example.tmovierestapi.payload.response;

import com.example.tmovierestapi.model.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PaymentMovieResponse {
    private Long id;

    @NotNull(message = "Imdb id must be greater than 0")
    private Long imdbID;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "origin_name")
    private String originName;

    @NotBlank
    @Size(max = 10000, message = "Content cannot greater than 4000 letter")
    private String content;

    @NotBlank
    private String type;

    @NotBlank
    private String thumbURL;

    @NotBlank
    private String trailerURL;

    @NotBlank
    private String time;

    @NotBlank
    private String quality;

    @NotBlank(message = "Slug must not empty")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private Integer year;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @NotBlank
    @Column(name = "poster_url")
    private String posterURL;

    @Column(name = "is_hot")
    private Boolean isHot;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @Column(name = "price", nullable = false)
    private Double price;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ", timezone = "UTC")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OrderBy("id ASC")
    private Set<Actor> actors = new HashSet<>();

    @OrderBy("id ASC")
    private Set<Director> directors = new HashSet<>();

    @OrderBy("id ASC")
    private Set<Category> categories = new HashSet<>();

    @OrderBy("name ASC")
    private Set<Episode> episodes = new HashSet<>();

    private Set<Comment> comments = new HashSet<>();


    public PaymentMovieResponse(Long id, Long imdbID, String name, String originName, String content, String type, String thumbURL, String trailerURL, String time, String quality, String slug, String posterURL, Integer year, Boolean isHot, Boolean isPremium, Double price, LocalDateTime createdDate, LocalDateTime modifiedDate, Country country, Set<Actor> actors, Set<Director> directors, Set<Category> categories, Set<Episode> episodes, Set<Comment> comments) {
        this.id = id;
        this.imdbID = imdbID;
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
