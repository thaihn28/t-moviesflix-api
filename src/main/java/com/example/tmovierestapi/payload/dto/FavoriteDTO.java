package com.example.tmovierestapi.payload.dto;

import com.example.tmovierestapi.payload.response.PlaylistResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class FavoriteDTO {
    private Long id;
    private UserDTO user;
    private Set<PlaylistResponse> playlists = new HashSet<>();
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
