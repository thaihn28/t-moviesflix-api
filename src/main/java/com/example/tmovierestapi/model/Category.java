package com.example.tmovierestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // important
@ToString(onlyExplicitlyIncluded = true) // important
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(name = "created_date")
    private Instant createdDate;

//    Quan hệ n-n với đối tượng ở dưới (Category) (1 category có nhiều movie)
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Include // không sử dụng trường này trong equals và hashcode
    @ToString.Include // Khoonhg sử dụng trong toString()
    private Set<Movie> movies = new HashSet<>();

}
