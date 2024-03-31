package com.pintogether.backend.websocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import javax.crypto.SecretKey;
@Component
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeInterceptor.class);

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String jwt = extractJwtFromRequest(request);
        if (jwt != null) {
            String memberId = validateAndExtractMemberIdFromJwt(jwt, response);
            if (memberId != null && !memberId.equals("anonymousUser")) {
                attributes.put("memberId", memberId);
                return true;
            }
        }
        return false;
    }

    private String extractJwtFromRequest(ServerHttpRequest request) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        logger.info("[{} {}] Authentication started. ", request.getMethod(), httpRequest.getRequestURI());
        Cookie[] cookies = httpRequest.getCookies();
        String jwt="";
        for (Cookie x : cookies) {
            if (x.getName().equals("Authorization")) {
                jwt = x.getValue();
                return jwt;
            }
        }
        return null;
    }

    private String validateAndExtractMemberIdFromJwt(String jwt, ServerHttpResponse response) {
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
        HttpServletResponse httpResponse = servletResponse.getServletResponse();
        try {
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            logger.info("Validating access token...");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            logger.info("id: {} member logged in.", claims.get("id"));
            return String.valueOf(claims.get("id"));
        } catch (JwtException je) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("JWT validation failed: " + je.getMessage());
        } catch (IllegalArgumentException ie) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Invalid argument: " + ie.getMessage());
        }
        return null;
    }
}

