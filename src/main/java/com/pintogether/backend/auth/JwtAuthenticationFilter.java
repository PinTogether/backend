package com.pintogether.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        String jwt="";
        for (Cookie x : cookies) {
            if (x.getName().equals("Authorization")) {
                jwt = x.getValue();
                break;
            }
        }
        if (jwt.isEmpty()) {
            throw new BadRequestException("jwt없음");
        }
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        String id = String.valueOf(claims.get("id"));
        String role = String.valueOf(claims.get("role"));

        GrantedAuthority a = new SimpleGrantedAuthority(role);
        var auth = new UsernamePasswordAuthenticationToken(id, null, List.of(a));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        /**
         * Authorization by JwtAuthenticationFilter
         * : /members/me/**
         *
         * Authorization from PreAuthorize Annotation
         * : members/{memberId},
         *
         */

        String requestURI = request.getRequestURI();
        String[] includePaths = {
//                "/search/history"
        };

        String[] excludedPaths = {
                "/",
                "/search/*"

//                "/oauth2/authorization/google",
//                "/oauth2/authorization/kakao",
//                "/oauth2/authorization/naver",
//                "/members/*",
        };
        for (String path : excludedPaths) {
            if (requestURI.equals(path)) {
                return true;
            }
        }
        return false;
    }
}