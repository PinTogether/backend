package com.pintogether.backend.exception;
import com.pintogether.backend.model.ErrorCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 검증 실패한 필드와 메시지를 추출
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input data");

        // 응답할 JSON 구조를 생성
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", ErrorCode.BAD_REQUEST.getCode());
        response.put("message", errorMessage);
        // 입력된 데이터를 포함. 실제 프로젝트에 따라 다르므로 적절히 조정이 필요할 수 있습니다.
        response.put("data", ex.getBindingResult().getTarget());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
