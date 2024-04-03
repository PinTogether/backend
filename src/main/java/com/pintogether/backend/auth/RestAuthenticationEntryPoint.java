package com.pintogether.backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        switch (response.getStatus()) {
            case 401 -> response.getWriter().write("{\"status\": {\"code\": " + response.getStatus() + ", \"message\": \"유효하지 않는 멤버입니다.\"}}");
            case 403 -> response.getWriter().write("{\"status\": {\"code\": " + response.getStatus() + ", \"message\": \"권한이 없습니다.\"}}");
            case 500 -> response.getWriter().write("{\"status\": {\"code\": " + response.getStatus() + ", \"message\": \"요청 처리 중 에러 발생.\"}}");
        }
    }

}
