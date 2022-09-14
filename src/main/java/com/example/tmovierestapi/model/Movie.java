package com.example.tmovierestapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "origin_name")
    private String originName;

    @NotBlank
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
    @Column(name = "episode_current")
    private String episodeCurrent;

    @NotBlank
    @Column(name = "episode_total")
    private String episodeTotal;

    @NotBlank
    private String quality;

    @NotBlank
    private String slug;

    private Integer year;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @NotBlank
    @Column(name = "poster_url")
    private String posterURL;

    @Column(name = "show_times")
    private String showTimes;

    @Column(name = "is_hot")
    private Boolean isHot;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "price")
    private Double price;


    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ", timezone = "UTC")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})

    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    @JoinTable(name = "movie_actor", //Tạo ra một join Table tên là "movie_actor"
            joinColumns = @JoinColumn(name = "movie_id"),  // TRong đó, khóa ngoại chính là movie_id trỏ tới class hiện tại (Movie)
            inverseJoinColumns = @JoinColumn(name = "actor_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Actor)
    )
    @OrderBy("id ASC")
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "movie_director",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    @OrderBy("id ASC")
    private Set<Director> directors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @OrderBy("id ASC")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("name ASC")
    private Set<Episode> episodes = new HashSet<>();

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getMovies().remove(this);
    }

    //Constructors, getters and setters removed for brevity
    public void removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.getMovies().remove(this);
    }

    public void removeDirector(Director director) {
        this.directors.remove(director);
        director.getMovies().remove(this);
    }

}
