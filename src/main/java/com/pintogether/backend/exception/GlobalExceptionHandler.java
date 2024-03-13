package com.pintogether.backend.exception;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletResponse response) {
        response.setStatus(StatusCode.BAD_REQUEST.getCode());
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input data");
        return new ApiResponse(StatusCode.BAD_REQUEST.getCode(), errorMessage, ex.getBindingResult().getTarget(), response);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ApiResponse customExceptionHandler(CustomException customException, HttpServletResponse response) {
        response.setStatus(customException.getStatusCode().getCode());
        return new ApiResponse(customException.getStatusCode().getCode(), customException.getMessage(), response);
    }
}