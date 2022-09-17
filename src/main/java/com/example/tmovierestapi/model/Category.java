package com.example.tmovierestapi.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotEmpty(message = "Slug is required")
    private String slug;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

//    Quan hệ n-n với đối tượng ở dưới (Category) (1 category có nhiều movie)
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    /* Important when using Lombok */
    @JsonIgnore
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Set<Movie> movies = new HashSet<>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Set<Playlist> playlists = new HashSet<>();

}
