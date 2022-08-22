package com.example.tmovierestapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String slug;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Playlist playlist;

}
