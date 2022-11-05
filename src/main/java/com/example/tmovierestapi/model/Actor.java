package com.example.tmovierestapi.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String avatar;

    @NotEmpty(message = "Slug is required")
    @Pattern(message = "Alphanumeric words in slug separated by single dashes (ex: standard-slug-pattern)",
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String slug;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "is_hot")
    private Boolean isHot;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    /* Important when using Lombok */
    @JsonIgnore
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Set<Movie> movies = new HashSet<>();
}
