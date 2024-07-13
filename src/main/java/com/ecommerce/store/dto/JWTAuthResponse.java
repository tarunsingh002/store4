package com.ecommerce.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private String refreshToken;
    private Long tokenExpirationTime;
    private Long refreshTokenExpirationTime;
    private boolean webmaster;
}
