package com.pintogether.backend.exception;

import com.pintogether.backend.model.ErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.AbstractMap;
import java.util.Map;

public class CustomException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;
    private String message;
    @Getter
    private Map.Entry<String, Object> data;

    // 생성자
    public CustomException(ErrorCode errorCode, String message, Object data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = new AbstractMap.SimpleEntry<>(data.getClass().getSimpleName(), data);
    }

    public String getMessage() {
        if(StringUtils.hasLength(this.message)) {
            return this.message;
        }
        return errorCode.getMessage();
    }

}
