package com.example.tmovierestapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Data
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "link_embed")
    private String linkEmbed;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Movie movie;

    @Column(name = "created_date")
    private Instant createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode )) return false;
        return id != null && id.equals(((Episode) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
