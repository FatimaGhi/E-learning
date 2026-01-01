package com.example.Mylab.shared;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionResponse {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<?>> HandleValidationException(MethodArgumentNotValidException ex) {

        var errors = ex.getBindingResult().getFieldErrors().stream().map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage())).toList();
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<GlobalResponse<?>> HandleCustemResException(CustomResponseException ex) {
        var errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.resolve(ex.getCode()));
    }
}
