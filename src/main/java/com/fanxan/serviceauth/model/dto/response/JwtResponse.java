package com.fanxan.serviceauth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JwtResponse {
    private Long id;

    @JsonProperty("access_token")
    private String accessToken;

    private String type = "Bearer";
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email,
                       List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}