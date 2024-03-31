package com.pintogether.backend.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum StatusCode {
    OK(200, "OK", HttpStatus.OK),
    BAD_REQUEST(400, "BAD REQUEST", HttpStatus.OK),

    CREATED(201, "CREATED", HttpStatus.CREATED),

    NO_CONTENT(204, "NO_CONTENT", HttpStatus.NO_CONTENT),

    FORBIDDEN(403, "FORBIDDEN", HttpStatus.FORBIDDEN),

    NOT_FOUND(404, "NOT_FOUND", HttpStatus.NOT_FOUND),

    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),

    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    @Getter
    private final int code;
    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;

    StatusCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
