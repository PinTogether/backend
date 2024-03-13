package com.pintogether.backend.exception;
import com.pintogether.backend.model.ApiResponse;
import com.pintogether.backend.model.StatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input data");
        return new ApiResponse(StatusCode.BAD_REQUEST.getCode(), errorMessage, ex.getBindingResult().getTarget());
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ApiResponse customExceptionHandler(CustomException customException) {
        return new ApiResponse(customException.getStatusCode().getCode(), customException.getMessage());
    }
}