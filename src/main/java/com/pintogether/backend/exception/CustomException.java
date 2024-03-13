package com.pintogether.backend.exception;

import com.pintogether.backend.model.StatusCode;
import lombok.Getter;
import org.springframework.util.StringUtils;
public class CustomException extends RuntimeException {
    @Getter
    private final StatusCode statusCode;
    private String message;

    // 생성자
    public CustomException(StatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        if(StringUtils.hasLength(this.message)) {
            return this.message;
        }
        return statusCode.getMessage();
    }
}
