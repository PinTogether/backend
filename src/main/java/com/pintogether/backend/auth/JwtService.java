package com.pintogether.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtService {

    @Value("${frontend.cookie.url}")
    private String cookieUrl;
    @Value("${jwt.signing.key}")
    private String signingKey;

<<<<<<< HEAD
    public String getRegistrationKey(String jwt) {
=======
    public String getRegistrationId(String jwt) {
>>>>>>> ddc274eded8d2db9c3079834de119c732b70390d
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
<<<<<<< HEAD
        return String.valueOf(claims.get("registrationKey"));
    }

    public Long getId(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return Long.parseLong(String.valueOf(claims.get("id")));
=======
        return String.valueOf(claims.get("registrationId"));
>>>>>>> ddc274eded8d2db9c3079834de119c732b70390d
    }

}
