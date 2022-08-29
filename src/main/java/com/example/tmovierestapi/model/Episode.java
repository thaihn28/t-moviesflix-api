package com.example.tmovierestapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    private String filename;

    @NotBlank(message = "Link embed is required")
    @Column(name = "link_embed")
    private String linkEmbed;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Movie movie;

    @Column(name = "created_date")
    private Instant createdDate;


}
