package com.example.tmovierestapi.payload.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
//    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
//        this.roles = roles;
    }
}
