package com.pintogether.backend.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ApiResponse<T> {
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Metadata metadata;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<T> results;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    // 생성자 - 정상 응답
    public ApiResponse(List<T> results) {
        this.status = new Status(StatusCode.OK.getCode(), StatusCode.OK.getMessage());
        this.results = results;
        this.metadata = new Metadata(results.size());
    }

    // 200 외 코드 응답
    public ApiResponse(List<T> results, StatusCode statusCode, HttpServletResponse response) {
        this.status = new Status(statusCode.getCode(), statusCode.getMessage());
        this.results = results;
        response.setStatus(statusCode.getCode());
        this.metadata = new Metadata(results.size());
    }

    public ApiResponse(int code, String message, HttpServletResponse response) {
        response.setStatus(code);
        this.status = new Status(code, message);
    }

    // 생성자 - 예외 응답
    public ApiResponse(int code, String message, Object data, HttpServletResponse response) {
        response.setStatus(code);
        this.status = new Status(code, message);
        this.data = data;
    }

    @Getter
    @AllArgsConstructor
    private static class Status {
        private int code;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    private static class Metadata {
        private int resultCount = 0;
    }

    public static <T> ApiResponse<T> makeResponse(List<T> result) {
        return new ApiResponse<>(result);
    }

    public static <T> ApiResponse<T> makeResponse(T result) {
        return makeResponse(Collections.singletonList(result));
    }

    public static ApiResponse makeResponse(int code, String message, HttpServletResponse response) {
        return new ApiResponse(code, message, response);
    }

    public static <T> ApiResponse makeResponse(T result, StatusCode statusCode, HttpServletResponse response) {
        return new ApiResponse(Collections.singletonList(result), statusCode, response);
    }

    public static <T> ApiResponse makeResponse(List<T> result, StatusCode statusCode, HttpServletResponse response) {
        return new ApiResponse(result, statusCode, response);
    }
}
