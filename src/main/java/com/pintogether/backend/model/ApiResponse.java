package com.pintogether.backend.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponse<T> {
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Metadata metadata;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> results;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    // 생성자 - 정상 응답
    public ApiResponse(List<T> results) {
        this.status = new Status(StatusCode.OK.getCode(), StatusCode.OK.getMessage());
        this.results = results;
        this.metadata = new Metadata(results.size());
    }

    public ApiResponse(int code, String message) {
        this.status = new Status(code, message);
    }

    // 생성자 - 예외 응답
    public ApiResponse(int code, String message, Object data) {
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
}
