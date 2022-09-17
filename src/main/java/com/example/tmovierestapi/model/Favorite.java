package com.example.tmovierestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Playlist> playlists = new HashSet<>();

    @OneToOne
    private User user;


}
