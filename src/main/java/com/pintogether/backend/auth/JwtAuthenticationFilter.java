package com.pintogether.backend.auth;

import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static com.pintogether.backend.model.ApiResponse.makeResponse;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        Cookie[] cookies = request.getCookies();

        String jwt="";
        System.out.println("Executing REQUEST " + requestURI );
        System.out.println("Method : " + request.getMethod());
        for (Cookie x : cookies) {
            if (x.getName().equals("Authorization")) {
                jwt = x.getValue();
                System.out.println("JWT " + jwt + " accepted.");
                break;
            }
        }
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        try {
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
        } catch (JwtException e) {
            makeResponse(403, "사용자 인증 실패", response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie x : cookies) {
            if (x.getName().equals("Authorization")) {
                return false;
            }
        }

        String[] permitPaths = {
                "/",
                "/members/{member_id:\\d+}",
                "/members/{member_id:\\d+}/collections/**",
                "/members/{member_id:\\d+}/scraps/**",
                "/collections/{\\d+}",
                "/collections/top",
                "/collections/{\\d+}/pins",
                "/collections/{\\d+}/comments",
                "/pin/{pin_id}/images",
                "/places/{place_id}/pins/**",
                "/places/{place_id}",
                "/search/place/**"
        };

        for (String path : permitPaths) {
            if (antMatcher(path).matches(request)) {
                return true;
            }
        }
        return false;
    }
}