package com.example.tmovierestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @NotBlank
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

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getMovies().remove(this);
    }

}
