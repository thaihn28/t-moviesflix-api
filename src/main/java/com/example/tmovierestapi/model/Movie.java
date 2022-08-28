package com.example.tmovierestapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
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

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ", timezone = "UTC")
    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    @JoinTable(name = "movie_actor", //Tạo ra một join Table tên là "movie_actor"
            joinColumns = @JoinColumn(name = "movie_id"),  // TRong đó, khóa ngoại chính là movie_id trỏ tới class hiện tại (Movie)
            inverseJoinColumns = @JoinColumn(name = "actor_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Actor)
    )
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "movie_director",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<Director> directors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Episode> episodes = new HashSet<>();

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
    //Constructors, getters and setters removed for brevity

    public void addEpisode(Episode episode) {
        episodes.add(episode);
        episode.setMovie(this);
    }

    public void removeEpisode(Episode episode) {
        episodes.remove(episode);
        episode.setMovie(null);
    }
}
