package com.pintogether.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.pintogether.backend.model.ApiResponse.makeResponse;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        Cookie[] cookies = request.getCookies();
        logger.info("[{} {}] Authentication started. ", request.getMethod(), requestURI);

        String jwt="";
        for (Cookie x : cookies) {
            if (x.getName().equals("Authorization")) {
                jwt = x.getValue();
                logger.info("JWT found.");
                break;
            }
        }
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        try {
            logger.info("Validating access token...");
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
            logger.info("id: {} member logged in.", id);
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("[{}] [{}] [{}]", requestMethod, requestURI, e.getMessage());
            makeResponse(401, "사용자 인증 실패", response);
        }
            filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie x : cookies) {
                if (x.getName().equals("Authorization")) {
                    logger.info("This request need to be authenticated in JwtFilter.");
                    return false;
                }
            }
        }
        List<AntPathRequestMatcher> permitPaths = new ArrayList<>();
        permitPaths.add(new AntPathRequestMatcher("/"));
        permitPaths.add(new AntPathRequestMatcher("/members/id/{member_id:\\d+}", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/members/{member_id:\\d+}/collections/**", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/members/{member_id:\\d+}/scraps/**", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/members/{membername:[a-zA-Z0-9_\\$]+}", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/collections**", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/collections/**", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/pins/{pin_id:\\d+}/images", "GET"));
        permitPaths.add(new AntPathRequestMatcher("/places/**"));
        permitPaths.add(new AntPathRequestMatcher("/places**"));
        permitPaths.add(new AntPathRequestMatcher("/search/collections**"));
        permitPaths.add(new AntPathRequestMatcher("/search/places**"));
        permitPaths.add(new AntPathRequestMatcher("/search/pins**"));
        permitPaths.add(new AntPathRequestMatcher("/search/members**"));

        for (AntPathRequestMatcher path : permitPaths) {
            if (path.matches(request)) {
                logger.info("This request passes the JwtFilter.");
                return true;
            }
        }
        logger.info("This request need to be authenticated in JwtFilter.");
        return false;
    }
}