package com.example.tmovierestapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
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

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    // Quan hệ n-n với đối tượng ở dưới (Person) (1 địa điểm có nhiều người ở)
//    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
//    @ToString.Exclude // Khoonhg sử dụng trong toString()
//    @JoinTable(name = "category_movie", //Tạo ra một join Table tên là "address_person"
//            joinColumns = @JoinColumn(name = "category_id"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
//            inverseJoinColumns = @JoinColumn(name = "movie_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
//    )
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JoinTable(name = "movie_category", //Tạo ra một join Table tên là "movie_category"
            joinColumns = @JoinColumn(name = "category_id"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
            inverseJoinColumns = @JoinColumn(name = "movie_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
    )
    private Set<Movie> movies = new HashSet<>();

}
