package com.pintogether.backend.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    OK(200, "OK", HttpStatus.OK),
    BAD_REQUEST(500, "BAD REQUEST", HttpStatus.OK)
    ;

    @Getter
    private final int code;
    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
