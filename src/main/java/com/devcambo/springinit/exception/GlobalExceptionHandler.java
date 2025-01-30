package com.devcambo.springinit.exception;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ErrorResponse(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleInvalidSortFieldException(InvalidSortFieldException ex) {
    return new ErrorResponse(false, StatusCode.BAD_REQUEST, ex.getMessage());
  }
}
