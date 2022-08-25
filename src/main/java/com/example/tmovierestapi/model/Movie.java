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

    @NotBlank
    @Column(name = "poster_url")
    private String posterURL;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ", timezone = "UTC")
    @Column(name = "created_date")
    private Instant createdDate;

    // mappedBy trỏ tới tên biến persons ở trong Address.
    @ManyToMany(mappedBy = "movies")
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(mappedBy = "movies")
    private Set<Director> directors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JoinTable(name = "movie_category", //Tạo ra một join Table tên là "movie_category"
            joinColumns = @JoinColumn(name = "movie_id"),  // TRong đó, khóa ngoại chính là address_id trỏ tới class hiện tại (Address)
            inverseJoinColumns = @JoinColumn(name = "category_id") //Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Person)
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private Set<Episode> episodes = new HashSet<>();
}
