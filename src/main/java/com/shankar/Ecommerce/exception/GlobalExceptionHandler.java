package com.shankar.Ecommerce.exception;

import com.shankar.Ecommerce.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handlerUnknownException(Exception ex) {
        ApiResponse<?> errorResponse = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlerNotFoundException(NotFoundException ex) {
        ApiResponse<?> errorResponse = ApiResponse.builder().status(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handlerInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiResponse<?> errorResponse = ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }


}
