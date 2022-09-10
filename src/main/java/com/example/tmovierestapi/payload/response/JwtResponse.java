package com.example.tmovierestapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
//    private List<String> roles;

    public JwtResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
//        this.roles = roles;
    }
}
