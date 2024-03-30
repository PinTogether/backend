package com.pintogether.backend.websocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.StatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
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
            String memberId = validateAndExtractMemberIdFromJwt(jwt);
            if (!memberId.equals("anonymousUser")) {
                attributes.put("memberId", memberId);
                return true;
            }
        }
        return false;
    }

    private String extractJwtFromRequest(ServerHttpRequest request) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpRequest = servletRequest.getServletRequest();
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

    private String validateAndExtractMemberIdFromJwt(String jwt) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return String.valueOf(claims.get("id"));
        } catch (JwtException je) {
            throw new CustomException(StatusCode.UNAUTHORIZED, "사용자 인증에 실패하였습니다.");
        } catch (IllegalArgumentException ie) {
            throw new CustomException(StatusCode.BAD_REQUEST, "요청 처리 중 에러가 발생하였습니다.");
        }
    }
}

