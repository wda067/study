package com.study.controller;

import com.study.exception.CustomException;
import com.study.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * 컨트롤러에서 발생하는 예외를 전역적으로 처리
 * @ExceptionHandler으로 특정 예외를 처리
 */
@RestControllerAdvice
public class ExceptionController {

    /**
     * MethodArgumentNotValidException은 Spring Validation를 사용할 때
     * 요청 본문에서 전달된 데이터가 유효성 검사에 실패하면 발생한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        e.getFieldErrors().forEach(error ->
                response.addValidation(error.getField(), error.getDefaultMessage()));

        return response;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CustomException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(e.getStatusCode())
                .body(response);
    }
}
