package com.example.tmovierestapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // Quan hệ n-n với đối tượng ở dưới (Person) (1 địa điểm có nhiều người ở)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JoinTable(name = "director_movie", //Tạo ra một join Table tên là "address_person"
            joinColumns = @JoinColumn(name = "director_id"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
            inverseJoinColumns = @JoinColumn(name = "movie_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
    )
    private Set<Movie> movies = new HashSet<>();
}
