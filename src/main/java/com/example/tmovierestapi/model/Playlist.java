package com.example.tmovierestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String originName;

    @NotBlank
    private String type;

    @NotBlank
    @Column(name = "thumb_url")
    private String thumbURL;

    @NotBlank
    private String episodeCurrent;

    @NotBlank
    private String time;

    @NotBlank
    private String quality;

    @NotBlank(message = "Slug must not empty")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    private int year;

    @NotNull
    @Column(name = "is_hot")
    private Boolean isHot = false;

    @NotNull
    @Column(name = "is_premium")
    private Boolean isPremium;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "playlist_category",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @OrderBy("id ASC")
    private Set<Category> categories = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToMany(mappedBy = "playlists", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Favorite> favorites;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getMovies().remove(this);
    }

    public void removeFavorite(Favorite favorite){
        this.favorites.remove(favorite);
        favorite.getPlaylists().remove(this);
    }

}
